$(function(){
    // 회원 유형 필드 및 관련 함수
    let mType = "m";
    let $memberTypeInput = $("input[name='memberType']"); // memberType element 가져옴
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
    // submit 시 emailId 와 emailDomain 을 합쳐 email을 별도로 정의, 할당할 필요있음
    let emailId = null;
    let $emailIdInput = $("input[name='emailId']");
    $emailIdInput.on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[0-9a-zA-Z][A-Za-z0-9\._-]{0,14}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        emailId = this.value;
    });
    let emailDomain = null;
    let $emailDomainInput = $("select[name='emailDomain']");
    $emailDomainInput.on("change", function(e){
        e.preventDefault();
        emailDomain = this.value;
    })

    // 회원 이름 필드 및 관련 함수
    let memberName = null;
    let $name = $("input[name='name']");
    $name.on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[ㄱ-힣]{0,10}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        memberName = this.value;
    })

    // 폰 번호 필드 및 관련 함수
    let firstPhNum = "010";
    let $firstPhNum = $("select[name='firstPhNum']");
    $firstPhNum.on("change", function(e){
        e.preventDefault();
        firstPhNum = this.value;
    })
    let secondPhNum = null;
    let $secondPhNum = $("input[name='secondPhNum']");
    $secondPhNum.on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[0-9]{0,4}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        secondPhNum = this.value;
    })
    let thirdPhNum = null;
    let $thirdPhNum = $("input[name='thirdPhNum']");
    $thirdPhNum.on("propertychange change paste input", function(e){
        e.preventDefault();
        const regExp = /^[0-9]{0,4}$/;
        if( !regExp.test(this.value) ){
            this.value = this.value.substring( 0 , this.value.length - 1 ); // 입력한 특수문자 한자리 지움
        }
        thirdPhNum = this.value;
    })

    // submit 버튼
    let $submitBtn = $("button[type='submit']");
    $submitBtn.on("click", submitHandler)

    // ajax 통신
    function submitHandler(e){
        e.preventDefault();
        let member = {};
        let memberId = origMemberId;
        let memberType = mType;
        let socialType = origSocialType;
        let email = emailId != null && emailDomain != null ? emailId + "@" + emailDomain : null;
        let name = memberName;
        let phNum = secondPhNum != null && secondPhNum != "" && thirdPhNum != null && thirdPhNum != "" ? firstPhNum + secondPhNum + thirdPhNum : null;

        member = {memberId, memberType , socialType, email, name, phNum};
        console.log(member);

        memberId == null || memberId == "" || socialType == null || socialType == "" ? history.go(-2) : "";
        email == null ? $("#emailTip").css("visibility", "visible") : $("#emailTip").css("visibility", "hidden");
        name == null ? $("#nameTip").css("visibility", "visible") : $("#nameTip").css("visibility", "hidden");
        phNum == null ? $("#phNumTip").css("visibility", "visible") : $("#phNumTip").css("visibility", "hidden");
        if(memberId != null && email != null && name != null && phNum != null){
            $.ajax({
                type: 'POST',
                url: '/api/member/join',
                contentType: 'application/json',
                data: JSON.stringify(member),
                success: function(result){
                    result.resultCode == 0 ? location.href="/" : alert("올바르지 않은 값이 입력되었습니다. 확인해주세요.");
                },
                error: function(result){
                    console.log("=== failed ===");
                    console.log(result);
                }
            })
        }
    }

});