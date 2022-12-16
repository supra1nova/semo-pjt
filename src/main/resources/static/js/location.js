$(function (){
    $.ajax({
        type: 'GET',
        url: '/api/product/info/' + productId,
        dataType: 'json',
        success: function(result){
            $('#productName').text(result.data.productInfo.productName + " 찾아오시는 길");
            $('#addressInput').text(result.data.productInfo.address + " " + result.data.productInfo.address);
            $('#zipCode').text(result.data.productInfo.zipCode.slice(0,3) + "-" + result.data.productInfo.zipCode.slice(3));
            if(result.data.productInfo.telNum[1] == 2) {
                $('#telInput').text(result.data.productInfo.telNum.slice(0,2) + "-" + result.data.productInfo.telNum.slice(2,6) + "-" + result.data.productInfo.telNum.slice(6));
            } else{
                $('#telInput').text(result.data.productInfo.telNum.slice(0,3) + "-" + result.data.productInfo.telNum.slice(3,7) + "-" + result.data.productInfo.telNum.slice(7));
            }
        },
        error: function(result){
            console.log("=== failed ===");
            console.log(result);
        }
    })
    let $container = $('#map').get(0);
    let options = { //지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
        level: 3 //지도의 레벨(확대, 축소 정도)
    };
    let map = new kakao.maps.Map($container, options); //지도 생성 및 객체 리턴
})