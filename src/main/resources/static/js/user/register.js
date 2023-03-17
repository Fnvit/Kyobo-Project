/******* form ********/
const registForm = document.forms.item(0);
/******* userEmail ********/
const userEmailInput = document.getElementById('user-email-div').firstElementChild;
/******* userBirth ********/
const userBirthDiv = document.getElementById('user-birth-div');
const userYearInput = userBirthDiv.firstElementChild;
const userMonthSelect = document.getElementById('user-month');
const userDaySelect = document.getElementById('user-day');
const userBirthInput = userBirthDiv.lastElementChild;
/******** Phone *********/
const userPhoneInput = document.getElementById('user-phone-div').firstElementChild;
const userPhoneRInput = document.getElementById('user-phone-div').lastElementChild;
const userPhoneAuthenticateDiv = document.getElementById('user-phone-authenticate-div');
const userPhoneAuthenticateInput = userPhoneAuthenticateDiv.firstElementChild;

/******** Btns **********/
const [registerBtn, cancelBtn] = [...document.getElementById('user-confirm-btn-div').getElementsByTagName('input')];

//do register
registerBtn.addEventListener('click', () => {
    //create userBirthValue
    userBirthInput.value = userYearInput.value + '-' + userMonthSelect.value + '-' + userDaySelect.value;
    userPhoneRInput.value = userPhoneInput.value;
    registForm.submit(); //do register
});

//do cancel
cancelBtn.addEventListener('click', () => {
    const cancelConfirm = confirm('정말로 회원가입을 취소하고 돌아가시겠습니까?');
    if(cancelConfirm) {
        location.href = '/user/login';
    }
});

create_user_month_value();
create_user_day_value();

//생년월일에서 월 생성 (select)
function create_user_month_value(){
    for (let i = 1; i <= 12; i++) {
        i = i >= 10 ? i : '0' + i;
        userMonthSelect.insertAdjacentHTML(
            'beforeend',
            `<option value="${i}">${i}</option>`
        );
    }
}
//생년월일에서 일 생성 (select)
function create_user_day_value(){
    for (let i = 1; i <= 31; i++) {
        i = i >= 10 ? i : '0' + i;
        userDaySelect.insertAdjacentHTML(
            'beforeend',
            `<option value="${i}">${i}</option>`
        );
    }
}
//휴대폰 번호 사이에 - 넣기 (현재 사용하지 않음)
function create_user_phone_value(){
    const userPhoneValue = userPhoneInput.value;
    const userPhoneLength = userPhoneValue.length;
    userPhoneRInput.value =
        userPhoneLength === 11 ?
            userPhoneValue.substring(0, 3) + '-' + userPhoneValue.substring(3, 7) + '-' + userPhoneValue.substring(7, 11) :
            userPhoneValue.substring(0, 3) + '-' + userPhoneValue.substring(3, 6) + '-' + userPhoneValue.substring(6, 10);
}


function email_authenticate(){
    fetch(`/api/user/${userEmailInput.value}`)
        .then(value => value.text())
        .then(value => {
            if(value === 'true'){
                alert('유저가 존재하기 때문에 불가능!')
            }else{
                alert('그 아이디 사용가능!');
            }
            console.log(value);
        })
        .catch(reason => {
            console.log(reason);
        })
}

// 휴대폰 인증번호 발송하기 버튼
function phoneNumber_authenticate(){
   fetch('/api/sms/' + userPhoneInput.value)
       .then(value => {
           console.log()
           if(value.ok) {
               return value.text();
           }
       })
       .then(value => {
           console.log(value)
       })
       .catch(reason => {
           console.log(reason);
       });
}

// 휴대폰으로 전송된 인증번호로 인증하기 버튼
function phoneNumber_authenticate_confirm(){
    fetch(`/api/sms?authNumber=${userPhoneAuthenticateInput.value}`)
        .then(value => {
            if(value.ok) {
                return value.text();
            }
        })
        .then(value => {
            console.log(value);
        })
        .catch(reason => {
            console.log(reason);
        });
}
















