if(loginInfo != null || "" || undefined){
    if(loginInfo.memberType == "c"){
        $.ajax({
            type: 'POST',
            url: '/api/product/info',
            contentType: 'application/json',
            data: JSON.stringify(loginInfo.id),
            success: function(result){
                let productIdGotten = result.resultCode == 0 ? result.data.productList[0].productId : null; // 업체 생성페이지로 이동하도록 처리 필요
                if(productIdGotten == productId){
                    let $AToBeAppended = $("<a>").prop("href", "/product/update/" + productId).text("내 업체 정보 수정하기");
                    let $additionalLiTag = $("<li>").attr("class", "sub-nav-li").append($AToBeAppended);
                    $(".sub-nav-ul").append($additionalLiTag);
                }
            },
            error: function(result){
                console.log("=== failed ===");
                console.log(result);
            }
        })
    }
}

$(function () {
    $('.post-wrapper').slick({
        slidesToShow: 2,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: 5000,
        prevArrow : $('.prevArrow'),
        nextArrow : $('.nextArrow'),
        speed: 1000, // 다음 버튼 누르고 다음 화면 뜨는데까지 걸리는 시간(ms)
    });

    $.ajax({
        type: 'GET',
        url: '/api/product/info/' + productId,
        dataType: 'json',
        success: function(result){
            $('#product-name').text(result.data.productInfo.productName).css('margin-top', "30px");
            $('#productDescription').text(result.data.productInfo.productDescription);
            if(result.data.productInfo.productDescription != null && result.data.productInfo.productDescription != ""){
                $('#product-presentation').show();
            }
            if(result.data.imageList != null && result.data.imageList != ""){
                for(let image of result.data.imageList){
                    let $divImg = $('<div>').addClass("post").css('margin-top', "20px");
                    let $carouselImg = $('<img>').attr("src", image);
                    $carouselImg.width('100%').height('100%');
                    $divImg.width('200px').height('300px');
                    $divImg.append($carouselImg);
                    $('#product-image').append($divImg);
                    $('.post-wrapper').slick('refresh');
                }
            }
        },
        error: function(result){
            console.log("=== failed ===");
            console.log(result);
        }
    });

});

