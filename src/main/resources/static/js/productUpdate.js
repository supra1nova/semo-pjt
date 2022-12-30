// sub nav 동적 생성
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
                    let $AToBeAppended = $("<a>").prop("href", "/product/update/" + productId).text("내 업체 정보 수정하기").attr("class", "currentBtn");
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

    let productInfo = null;

    $.ajax({
        type: 'GET',
        url: '/api/product/info/' + productId,
        dataType: 'json',
        success: function(result){
            productInfo = result.data.productInfo;
            $("#productName").val(productInfo.productName);
            if(productInfo.productCategory == "h") {
                $("input[name='productCategory']")[0].checked == true;
                $("input[name='productCategory']")[1].checked == false;
                $("input[name='productCategory']")[2].checked == false;
                $("#firstLabel").css({"background-color": "rgba(102,102,255,255)", "color": "white"});
                $("#secondLabel").css({"background-color" : "white", "color" : "black"});
                $("#thirdLabel").css({"background-color" : "white", "color" : "black"});
            } else if(productInfo.productCategory == "m"){
                $("input[name='productCategory']")[1].checked == true;
                $("input[name='productCategory']")[0].checked == false;
                $("input[name='productCategory']")[2].checked == false;
                $("#firstLabel").css({"background-color" : "white", "color" : "black"});
                $("#secondLabel").css({"background-color": "rgba(102,102,255,255)", "color": "white"});
                $("#thirdLabel").css({"background-color" : "white", "color" : "black"});
            } else{
                $("input[name='productCategory']")[2].checked == true;
                $("input[name='productCategory']")[0].checked == false;
                $("input[name='productCategory']")[1].checked == false;
                $("#firstLabel").css({"background-color" : "white", "color" : "black"});
                $("#secondLabel").css({"background-color" : "white", "color" : "black"});
                $("#thirdLabel").css({"background-color": "rgba(102,102,255,255)", "color": "white"});
            }
            $("#address").val(productInfo.address);
            $("#addressDetail").val(productInfo.addressDetail);
            $("#zipCode").val(productInfo.zipCode);
            $("#telNum").val(productInfo.telNum);
            $("#productDescription").val(productInfo.productDescription);
        },
        error: function(result){
            console.log("=== failed ===");
            console.log(result);
        }
    });

    // productName 변경시 일어날 이벤트
    $("#productName").on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[ㄱ-힣A-Za-z0-9\.\s_-]{0,10}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        productInfo.productName = this.value;
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
            productInfo.productCategory = "h";
        } else if($productCategoryInput[1].checked==true){
            $productCategoryInput[0].checked=false;
            $productCategoryInput[2].checked=false;
            $("#firstLabel").css({"background-color" : "white", "color" : "black"});
            $("#secondLabel").css({"background-color": "rgba(102,102,255,255)", "color": "white"});
            $("#thirdLabel").css({"background-color" : "white", "color" : "black"});
            productInfo.productCategory = "m";
        } else{
            $productCategoryInput[0].checked=false;
            $productCategoryInput[1].checked=false;
            $("#firstLabel").css({"background-color" : "white", "color" : "black"});
            $("#secondLabel").css({"background-color" : "white", "color" : "black"});
            $("#thirdLabel").css({"background-color": "rgba(102,102,255,255)", "color": "white"});
            productInfo.productCategory = "p";
        }
    });

    // address 변경시 일어날 이벤트
    $("#address").on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[ㄱ-힣A-Za-z0-9\.\s()@,_-]{0,100}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        productInfo.address = this.value;
    });

    // addressDetail 변경시 일어날 이벤트
    $("#addressDetail").on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[ㄱ-힣A-Za-z0-9\.\s()@,_-]{0,100}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        productInfo.addressDetail = this.value;
    });

    // zipCode 변경시 일어날 이벤트
    $("#zipCode").on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[0-9]{0,6}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        productInfo.zipCode = this.value;
    });

    // telNum 변경시 일어날 이벤트
    $("#telNum").on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[0-9]{0,11}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        productInfo.telNum = this.value;
    });

    // productDescription 변경시 일어날 이벤트
    $("#productDescription").on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[ㄱ-힣ㄱ-ㅎㅏ-ㅣA-Za-z0-9\.\s\n!?#$%&*+=()@,'"\[\]_-]{0,100}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        productInfo.productDescription = this.value;
    });

    // 취소 버튼 이벤트
    $("#cancelBtn").on("click", function(e){
        e.preventDefault();
        location.href="/product/info/" + productId;
    })

    // 수정 버튼 이벤트
    $("#submitBtn").on("click", submitHandler)

    // 수정 버튼 이벤트 핸들러
    function submitHandler(e){
        e.preventDefault();
        // 검증 관련해 코드 추가할 필요가 있음.
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
                url: '/api/product/edit',
                contentType: 'application/json',
                data: JSON.stringify(productInfo),
                success: function(result){
                    console.log(result);
                    result.resultCode == 0 ? location.href="/product/info/" + productId : alert("올바르지 않은 값이 입력되었습니다. 확인해주세요.");
                },
                error: function(result){
                    console.log("=== failed ===");
                    console.log(result);
                }
            })
        // }
    }

    const $modal = $("#modal");
    $("#deleteBtn").on("click", function(e) {
        e.preventDefault();
        window.scrollTo(0,0);
        $modal.css("visibility", "visible");
    })

    $modal.on("click", function(e){
        const evTarget = e.target;
        if(evTarget.classList.contains("modal-overlay")) {
            $modal.css("visibility", "hidden");
        }
    })

    window.addEventListener("keyup", e => {
        if($modal.css("visibility") === "visible" && e.key === "Escape") {
            $modal.css("visibility", "hidden");
        }
    })

    const $closeArea = $(".close-area");
    $closeArea.on("click", function(e){
        e.preventDefault();
        $modal.css("visibility", "hidden");
    })

    const $deleteCancelBtn = $("#deleteCancelBtn");
    $deleteCancelBtn.on("click", function(e){
        e.preventDefault();
        $modal.css("visibility", "hidden");
    })

    const $deleteConfirmBtn = $("#deleteConfirmBtn");
    $deleteConfirmBtn.on("click", function(e){
        e.preventDefault();
        console.log("test");
        $.ajax({
            type: 'POST',
            url: '/api/product/delete?productId=' + productId,
            // contentType: 'application/json',
            // data: JSON.stringify(productId),
            success: function(result){
                console.log(result);
                result.resultCode == 0 ? location.href="/" : alert("올바르지 않은 값이 입력되었습니다. 확인해주세요.");
            },
            error: function(result){
                console.log("=== failed ===");
                console.log(result);
            }
        })
    })

});

