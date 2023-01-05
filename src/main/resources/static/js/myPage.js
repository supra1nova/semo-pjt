$(function(){
    console.log(loginInfo);

    // 회원 유형 필드 및 관련 함수
    let mType = loginInfo.memberType;
    let $memberTypeInput = $("input[name='memberType']"); // memberType element 가져옴
    if(loginInfo.memberType =="m") {
        $memberTypeInput[0].checked=true;
        $memberTypeInput[1].checked=false;
        $("#firstLabel").css({"background-color": "rgba(102,102,255,255)", "color": "white"});
        $("#secondLabel").css({"background-color": "white", "color" : "black"});
    } else {
        $memberTypeInput[0].checked=false;
        $memberTypeInput[1].checked=true;
        $("#firstLabel").css({"background-color" : "white", "color" : "black"});
        $("#secondLabel").css({"background-color": "rgba(102,102,255,255)", "color": "white"});
    }
    $memberTypeInput.on("click", function(e){
        e.preventDefault();
        if($memberTypeInput[1].checked==true){
            $memberTypeInput[0].checked=false;
            $("#firstLabel").css({"background-color" : "white", "color" : "black"});
            $("#secondLabel").css({"background-color": "rgba(102,102,255,255)", "color": "white"});
            mType = "c";
        } else{
            $memberTypeInput[1].checked=false;
            $("#firstLabel").css({"background-color": "rgba(102,102,255,255)", "color": "white"});
            $("#secondLabel").css({"background-color": "white", "color" : "black"});
            mType = "m";
        }
    });

    // 회원 이메일 아이디/도메인 관련 필드 및 관련 함수
    let emailId = loginInfo.email.substring(0,loginInfo.email.indexOf("@"));
    let $emailIdInput = $("input[name='emailId']");
    $emailIdInput.val(emailId);
    $emailIdInput.on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[0-9a-zA-Z][A-Za-z0-9\._-]{0,14}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        emailId = this.value;
    });
    let emailDomain = loginInfo.email.substring(loginInfo.email.indexOf("@") + 1);
    let $emailDomainInput = $("select[name='emailDomain']");
    $emailDomainInput.val(emailDomain);
    $emailDomainInput.on("change", function(e){
        e.preventDefault();
        emailDomain = this.value;
    })

    // 회원 이름 필드 및 관련 함수
    let memberName = loginInfo.name;
    let $name = $("input[name='name']");
    $name.val(memberName);
    $name.on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[ㄱ-힣]{0,10}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        memberName = this.value;
    })

    // 폰 번호 필드 및 관련 함수
    let firstPhNum = null;
    let secondPhNum = null;
    let thirdPhNum = null;
    let $firstPhNum = $("select[name='firstPhNum']");
    let $secondPhNum = $("input[name='secondPhNum']");
    let $thirdPhNum = $("input[name='thirdPhNum']");
    $.ajax({
        type: 'GET',
        url: '/api/member/info?email=' + loginInfo.email,
        success: function(result){
            if(result.data.phNum.length == 10){
                $firstPhNum.val(result.data.phNum.substring(0, 3));
                $secondPhNum.val(result.data.phNum.substring(3, 6));
                $thirdPhNum.val(result.data.phNum.substring(6, 10));
                firstPhNum = result.data.phNum.substring(0, 3);
                secondPhNum = result.data.phNum.substring(3, 6);
                thirdPhNum = result.data.phNum.substring(6, 10);
            } else if(result.data.phNum.length == 11){
                $firstPhNum.val(result.data.phNum.substring(0, 3));
                $secondPhNum.val(result.data.phNum.substring(3, 7));
                $thirdPhNum.val(result.data.phNum.substring(7, 11));
                firstPhNum = result.data.phNum.substring(0, 3);
                secondPhNum = result.data.phNum.substring(3, 7);
                thirdPhNum = result.data.phNum.substring(7, 11);
            }
        },
        error: function(result){
            console.log("=== failed ===");
            console.log(result);
        }
    })


    $firstPhNum.on("change", function(e){
        e.preventDefault();
        firstPhNum = this.value;
        $firstPhNum.val(this.value);
    })
    $secondPhNum.on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[0-9]{0,4}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        secondPhNum = this.value;
    })
    $thirdPhNum.on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[0-9]{0,4}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        thirdPhNum = this.value;
    })

    // submit 버튼
    let $submitBtn = $("#submitBtn");
    $submitBtn.on("click", submitHandler)

    // ajax 통신
    function submitHandler(e){
        e.preventDefault();
        let member = {};
        let memberId = loginInfo.id;
        let memberType = mType;
        let socialType = loginInfo.socialType;
        let email = emailId != null && emailDomain != null ? emailId + "@" + emailDomain : null;
        let name = memberName;
        let phNum = secondPhNum != null && secondPhNum != "" && thirdPhNum != null && thirdPhNum != "" ? firstPhNum + secondPhNum + thirdPhNum : null;

        member = {memberId, memberType , socialType, email, name, phNum};

        memberId == null || memberId == "" || socialType == null || socialType == "" ? history.go(-2) : "";
        email == null ? $("#emailTip").css("visibility", "visible") : $("#emailTip").css("visibility", "hidden");
        name == null ? $("#nameTip").css("visibility", "visible") : $("#nameTip").css("visibility", "hidden");
        phNum == null ? $("#phNumTip").css("visibility", "visible") : $("#phNumTip").css("visibility", "hidden");

        if(memberId != null && email != null && name != null && phNum != null){
            $.ajax({
                type: 'POST',
                url: '/api/member/edit',
                contentType: 'application/json',
                data: JSON.stringify(member),
                success: function(result){
                    result.resultCode == 0 ? location.href="/mypage" : alert("올바르지 않은 값이 입력되었습니다. 확인해주세요.");
                },
                error: function(result){
                    console.log("=== failed ===");
                    console.log(result);
                }
            })
        }
    }

});