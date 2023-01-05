let kakaoLoginBtn;
let naverLoginBtn;
window.onload = function() {
    kakaoLoginBtn = document.querySelector('#kakaoLoginBtn');
    kakaoLoginBtn.addEventListener("click", getKakaoLoginAuthURL);
    naverLoginBtn = document.querySelector('#naverLoginBtn');
    naverLoginBtn.addEventListener("click", getNaverLoginAuthURL);
}
function getKakaoLoginAuthURL(){
    location.href="https://kauth.kakao.com/oauth/authorize?client_id=e64b7d5f26b6b46201c22456474bc99c&response_type=code&redirect_uri=http://semo.com/login/kakao";
}
async function getNaverLoginAuthURL(){
    const naverURL = "http://semo.com/api/login/naver/auth-url";
    const authURL = await fetch(naverURL)
        .then(res => res.json())
        .then(data => data.data);
    location.href=authURL;
}