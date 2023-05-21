const toggleSidebar =() => {
    if($(".sidebar").is(":visible"))
    {
       $(".sidebar").css("display","none");
       $(".content").css("margin-left","0%");
    }
    else{
        $(".sidebar").css("display","block");
       $(".content").css("margin-left","20%");
    }
}


const search = () => {
    let query = $("#searchInput").val();

    if(query=='')
    {
        $(".search-result").hide();
    }
    else{
        console.log(query);
       

        //sending request to server

        let url = `http://localhost:8282/search/${query}`;

        fetch(url)                       // internally calls the url as a ajax call
        .then((response) => {
            return response.json();     // parsing the response to json
        }).then((data) =>{
            console.log(data);        //fetching data
        

        let text =`<div class='list-group'>`;

        data.forEach(contact => {
            text += `<a href='/user/contactDetails/${contact.cid}' class='list-group-item list-group-item-action'> ${contact.name} </a>`;
        });

        text+= `</div>`

        $(".search-result").html(text);
        $(".search-result").show();
    });
    }

}