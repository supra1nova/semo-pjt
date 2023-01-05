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