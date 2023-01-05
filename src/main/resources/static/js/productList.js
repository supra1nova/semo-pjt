$(function () {
    let productList = [];
    $.ajax({
        type: 'GET',
        url: '/api/product/list/',
        dataType: 'json',
        success: function(result){
            console.log("=== success ===");
            // console.log(result.data);
            if(result.data.length > 0){
                for(let i = 0; i < result.data.length; i++){
                    console.log(result.data[i]);
                    let $divToBeAppended = $("<a>").text(result.data[i].productName).attr("href", "/product/info/" + result.data[i].productId);
                    let $additionalDivTag = $("<div>").attr("class", "product-list-content").append($divToBeAppended);
                    $(".product-list-wrapper").append($additionalDivTag);
                }
            }
        },
        error: function(result){
            console.log("=== failed ===");
            console.log(result);
        }
    });
});

