package com.smart.contactmanager.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.contactmanager.dao.ContactRepository;
import com.smart.contactmanager.dao.UserRepository;
import com.smart.contactmanager.entities.Contact;
import com.smart.contactmanager.entities.User;


@Controller
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;
   
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @ModelAttribute
    public void commonDataforUser(Model model,Principal principal){
        String userName = principal.getName();
        System.out.println("Username "+userName);
        User user = userRepository.getUserByUsername(userName);
        System.out.println("User Details "+user);
        model.addAttribute("user", user);
    }

    @GetMapping
    public String dashBoard(Model model,Principal principal){
        model.addAttribute("title", "User Dashboard");
        return "user/index";
    }

    @GetMapping("/addContact")
    public String openAddContactForm(Model model)
    {
        model.addAttribute("title"," Add Contact");
        model.addAttribute("contact", new Contact());
        return "user/addContact";
    }

    @PostMapping("/addContact")
    public String submitForm(@ModelAttribute Contact contact,@RequestParam("contactImage") MultipartFile file,Principal principal) { 
        try {
        System.out.println("Inside Submit");
        System.out.println(contact);
        String name = principal.getName();
        System.out.println(name);
        System.out.println(contact.getCid()+"Contact ID");
        System.out.println(contact.getUser());

        System.out.println(file.getOriginalFilename());
        if(file.isEmpty())
        {
            System.out.println("File is empty");
            contact.setImagePath("default.png");
        }
        else{
            contact.setImagePath(file.getOriginalFilename());

           File saveFile = new ClassPathResource("static/img").getFile();

           Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
           Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

           System.out.println("IMage is uploaded");
        }

       
        User user = this.userRepository.getUserByUsername(name);
        user.getContacts().add(contact);
        contact.setUser(user);
        this.userRepository.save(user);
        

        

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Error "+e.getMessage());
            e.printStackTrace();
        }
        return "user/addContact";
    }

    @GetMapping({"/Contacts/{page}","/Contacts"})
    public String getAllContacts(@PathVariable(name="page",required = false) Integer page,Model model,Principal principal){
       String userName = principal.getName();
       User user = this.userRepository.getUserByUsername(userName);

       if(page == null)
       {
        page=0;
       }
       Pageable pageable = PageRequest.of(page, 1);

       Page<Contact> contactsByUser= this.contactRepository.findContactsByUser(user.getId(),pageable);

        model.addAttribute("title"," Add Contact");
        model.addAttribute("contacts", contactsByUser);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", contactsByUser.getTotalPages());

        System.out.println(contactsByUser.getTotalPages());
        System.out.println(page);

        System.out.println(contactsByUser);
      return "user/contacts";
    }

    @GetMapping("/contactDetails/{cid}")
    public String contactDetails(@PathVariable("cid") Integer cid,Model model,Principal principal){
        Optional<Contact> optionalContact = this.contactRepository.findById(cid);
        Contact contact = optionalContact.get();
        
        
        String userName = principal.getName();

        User user = this.userRepository.getUserByUsername(userName);

        if(user.getId() == contact.getUser().getId());
        {
            model.addAttribute("contactById", contact);
        }

        return "user/contactDetails";
    }

    @GetMapping("/Delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cid,Principal principal)
    {
            Contact contact = this.contactRepository.findById(cid).get();

            String userName = principal.getName();

            User user = this.userRepository.getUserByUsername(userName);

            if(user.getId() == contact.getUser().getId())
            {
                contact.setUser(null);                    // setting so that bidirection mapping should be destroyed
                this.contactRepository.delete(contact);
                System.out.println("Inside");
                System.out.println(contact);
            }

            return "redirect:/user/Contacts";

    }

    @GetMapping("/settings")
    public String openSettings(){
        return "user/settings";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("oldPass")  String oldPass, @RequestParam("newPass") String newPass,Principal principal){

        String userName = principal.getName();
        User currentUser = this.userRepository.getUserByUsername(userName);

        System.out.println(currentUser.getPassword());

        if(this.bCryptPasswordEncoder.matches(oldPass, currentUser.getPassword()))
        {
            System.out.println("Match");
            currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPass));

            this.userRepository.save(currentUser);
        }
        else{
            System.out.println(" Password doesn't match");
        }

        return "user/index";
    }


    @GetMapping("/updateContact/{id}")
    public String updateContact(@PathVariable("id") Integer id,Principal principal,Model model){

        Contact contact = this.contactRepository.findById(id).get();

        String username = principal.getName();

        User user =this.userRepository.getUserByUsername(username);

        if(user.getId()== contact.getUser().getId())
        {
            model.addAttribute("title", "Edit Contact");
            model.addAttribute("contact",contact);
        }
        else{

        }
        return "user/addContact";
    }

    @PostMapping("/updateContactP/{id}")
    public String updateForm(@ModelAttribute Contact contact , @PathVariable("id")Integer id,Model m,
    @RequestParam("contactImage")MultipartFile file, Principal principal)
    {
        try {
            
            Contact oldContact= this.contactRepository.findById(id).get();
            if(!file.isEmpty())
            {
                //delete old photo upload new photo

                File deleteFile = new ClassPathResource("static/img").getFile();
                File file1 = new File(deleteFile, oldContact.getImagePath());
                file1.delete();
            
            contact.setImagePath(file.getOriginalFilename());
            File saveFile = new ClassPathResource("static/img").getFile();

            Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
            Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("IMage is uploaded");
            contact.setImagePath(file.getOriginalFilename());
            }
            else{
                // replace existing 

                contact.setImagePath(oldContact.getImagePath());
            }

            User user = this.userRepository.getUserByUsername(principal.getName());
            contact.setUser(user);
            this.contactRepository.save(contact);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return "redirect:/user/Contacts";
    }

    @GetMapping("/profile")
    public String viewUserProfile(Principal principal,ModelMap model)
    {
        String username = principal.getName();
        User user = this.userRepository.getUserByUsername(username);

        model.addAttribute("title", "User Dashboard");
        model.addAttribute("user", user);

        return "/user/userProfile";
    }

    @PostMapping("/proFileImgUpload")
    public ResponseEntity<?> handleProfileImageUpload (@RequestParam("profileImage") MultipartFile imageFile,
    Principal principal) throws IOException
    {
        String userEmail = principal.getName();
        User user = this.userRepository.getUserByUsername(userEmail);

        if(!imageFile.isEmpty())
        {
            File folder = new ClassPathResource("/static/img").getFile();
            File deleteFile = new File(folder, user.getImageUrl());
            deleteFile.delete();

            

            Path imagePath = Paths.get(folder.getAbsolutePath()+File.separator+imageFile.getOriginalFilename());

            Files.copy(imageFile.getInputStream(),imagePath,StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Imaged Uploaded");
            user.setImageUrl(imageFile.getOriginalFilename());

            this.userRepository.save(user);
        }

        return ResponseEntity.ok("Image Uploded");
    }
}
