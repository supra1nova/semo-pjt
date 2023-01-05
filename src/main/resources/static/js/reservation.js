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

$(function (){
    // date picker 관련 내용
    let today = new Date();
    let picker = tui.DatePicker.createRangePicker({
        language: 'ko',
        startpicker: {
            date: today,
            weekStartDay: 'Sun',
            input: "#startpicker-input",
            container: "#startpicker-container",
        },
        endpicker: {
            date: today,
            input: '#endpicker-input',
            container: '#endpicker-container',
        },
        format: 'yyyy-MM-dd',
        selectableRanges: [
            [today, new Date(today.getFullYear() + 1, today.getMonth(), today.getDate())]
        ]
    });
    $('#personNumPlusBtn').click(function(){
        $('#personNumInput').val(parseInt($('#personNumInput').val()) + 1);
    })
    $('#personNumMinusBtn').click(function(){
        if(parseInt($('#personNumInput').val()) > 0) {
            $('#personNumInput').val(parseInt($('#personNumInput').val()) - 1);
        }
    })
})