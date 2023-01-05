$(function () {

    let productInfo = {};
    console.log(loginInfo.id)
    productInfo.memberId = loginInfo.id;
    console.log(productInfo.memberId)

    // productName 변경시 일어날 이벤트
    $("#productName").on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[ㄱ-힣A-Za-z0-9\.\s_-]{0,10}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        let productName = this.value;
        productInfo.productName = productName;
    });

    // productCategory 변경시 일어날 이벤트
    $("input[name='productCategory']").on("click", function(e){
        e.preventDefault();
        let $productCategoryInput = $("input[name='productCategory']");
        if($productCategoryInput[0].checked==true){
            $productCategoryInput[1].checked=false;
            $productCategoryInput[2].checked=false;
            $("#firstLabel").css({"background-color": "rgba(102,102,255,255)", "color": "white"});
            $("#secondLabel").css({"background-color" : "white", "color" : "black"});
            $("#thirdLabel").css({"background-color" : "white", "color" : "black"});
            let productCategory = "h";
            productInfo.productCategory = productCategory;
        } else if($productCategoryInput[1].checked==true){
            $productCategoryInput[0].checked=false;
            $productCategoryInput[2].checked=false;
            $("#firstLabel").css({"background-color" : "white", "color" : "black"});
            $("#secondLabel").css({"background-color": "rgba(102,102,255,255)", "color": "white"});
            $("#thirdLabel").css({"background-color" : "white", "color" : "black"});
            let productCategory = "m";
            productInfo.productCategory = productCategory;
        } else{
            $productCategoryInput[0].checked=false;
            $productCategoryInput[1].checked=false;
            $("#firstLabel").css({"background-color" : "white", "color" : "black"});
            $("#secondLabel").css({"background-color" : "white", "color" : "black"});
            $("#thirdLabel").css({"background-color": "rgba(102,102,255,255)", "color": "white"});
            let productCategory = "p";
            productInfo.productCategory = productCategory;
        }
    });

    // address 변경시 일어날 이벤트
    $("#address").on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[ㄱ-힣A-Za-z0-9\.\s()@,_-]{0,100}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        let address = this.value;
        productInfo.address = address;
    });

    // addressDetail 변경시 일어날 이벤트
    $("#addressDetail").on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[ㄱ-힣A-Za-z0-9\.\s()@,_-]{0,100}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        let addressDetail = this.value;
        productInfo.addressDetail = addressDetail;
    });

    // zipCode 변경시 일어날 이벤트
    $("#zipCode").on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[0-9]{0,6}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        let zipCode = this.value;
        productInfo.zipCode = zipCode;
    });

    // telNum 변경시 일어날 이벤트
    $("#telNum").on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[0-9]{0,11}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        let telNum = this.value;
        productInfo.telNum = telNum;
    });

    // productDescription 변경시 일어날 이벤트
    $("#productDescription").on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[ㄱ-힣A-Za-z0-9\.\s\n!?#$%&*+=()@,'"\[\]_-]{0,500}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        let productDescription = this.value;
        productInfo.productDescription = productDescription;
    });

    // 취소 버튼 이벤트
    $("#cancelBtn").on("click", function(e){
        e.preventDefault();
        location.href="/";
    })

    // 생성 버튼 이벤트
    $("#submitBtn").on("click", submitHandler)

    // 수정 버튼 이벤트 핸들러
    function submitHandler(e){
        e.preventDefault();
        // let member = {} ;
        // let memberId = origMemberId;
        // let memberType = mType;
        // let socialType = origSocialType;
        // let email = emailId != null && emailDomain != null ? emailId + "@" + emailDomain : null;
        // let name = memberName;
        // let phNum = secondPhNum != null && secondPhNum != "" && thirdPhNum != null && thirdPhNum != "" ? firstPhNum + secondPhNum + thirdPhNum : null;
        //
        // member = {memberId, memberType , socialType, email, name, phNum};
        // console.log(member);
        //
        // memberId == null || memberId == "" || socialType == null || socialType == "" ? history.go(-2) : "";
        // email == null ? $("#emailTip").css("visibility", "visible") : $("#emailTip").css("visibility", "hidden");
        // name == null ? $("#nameTip").css("visibility", "visible") : $("#nameTip").css("visibility", "hidden");
        // phNum == null ? $("#phNumTip").css("visibility", "visible") : $("#phNumTip").css("visibility", "hidden");
        // if(memberId != null && email != null && name != null && phNum != null){
            $.ajax({
                type: 'POST',
                url: '/api/product/create',
                contentType: 'application/json',
                data: JSON.stringify(productInfo),
                success: function(result){
                    console.log(result);
                    result.resultCode == 0 ? location.href="/" : alert("올바르지 않은 값이 입력되었습니다. 확인해주세요.");
                },
                error: function(result){
                    console.log("=== failed ===");
                    console.log(result);
                }
            })
        // }
    }
});

