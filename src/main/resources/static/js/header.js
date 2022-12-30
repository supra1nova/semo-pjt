const navElems = document.getElementsByClassName("nav-elems");
const $loginBtn = $("#loginBtn");

if(loginInfo && (loginInfo.memberType != "u")){
    $loginBtn.text("로그아웃");
    $loginBtn.on("click", logout);
    async function logout(e){
        e.preventDefault();
        const logoutUrl = loginInfo.socialType == "k" ? "/api/login/kakao/logout" : "/api/login/naver/logout";
        await fetch(logoutUrl)
        location.href="/";
    }
    if(loginInfo.memberType == "c"){
        $(".nav-elems").css("display", "flex");
        let $myBusinessBtn = $("div[name='myBusinessBtn']");
        $myBusinessBtn.on("click", getProductIdHandler);
    }
} else{
    for (const elem of navElems) {
        elem.style.visibility = 'hidden';
    }
    $loginBtn.on("click", function(e){
        e.preventDefault();
        location.href="/login";
    })
}

function getProductIdHandler(e){
    e.preventDefault();
    console.log(loginInfo.id);
    $.ajax({
        type: 'POST',
        url: '/api/product/info',
        contentType: 'application/json',
        data: JSON.stringify(loginInfo.id),
        success: function(result){
            result.resultCode == 0 ? location.href="/product/info/" + result.data.productList[0].productId : location.href="/product/create"; // 업체 생성페이지로 이동하도록 처리 필요
        },
        error: function(result){
            console.log("=== failed ===");
            console.log(result);
        }
    })
}