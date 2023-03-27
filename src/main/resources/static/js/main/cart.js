const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
//선택관련
const selectAllBtn = document.getElementById('cart-result-container').querySelector('i');
const removeAllBtn = document.getElementById('remove-all-btn');
//장바구니관련
const bookCartContainer = document.getElementById('book-cart-container');
const bookInfoContainer = document.getElementsByClassName('book-info-container');
//찜관련
const heartAllBtn = document.getElementById('heart-all-btn');
//주문/결제 관련
const orderInfoContainer = document.getElementById('order-info-container');
const orderProceedBtn = document.getElementById('order-proceed-btn');

get_cart();

function get_cart(){
    fetch('/user/cart')
        .then(response => response.json())
        .then(value => { create_book_list_in_cart(value); });
}

function modify_cart(btn, operator){
    const bookISBN = btn.parentElement.parentElement.parentElement.querySelector('.book-info-isbn').value;
    const bookCountInput = btn.parentElement.querySelector('input');
    const bookCount = operator === '+' ? +bookCountInput.value + 1 : +bookCountInput.value - 1;
    fetch('/user/cart', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            "X-CSRF-TOKEN": csrfToken
        },
        body: JSON.stringify({bookISBN: bookISBN, bookCount: bookCount})})
        .then(value => value.text())
        .then(value => {
            if(value == 'true') get_cart();
        });
        // .catch(reason => {log.info(reason)});
}

function delete_cart(){
    const containers = get_clicked_boxes();
    const body = [];
    containers.forEach(container => {
        const isbnValue = container.querySelector('.book-info-isbn').value;
        body.push({bookISBN: isbnValue});
    });

    fetch('/user/cart', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            "X-CSRF-TOKEN": csrfToken
        },
        body: JSON.stringify(body)})
        .then(value => value.text())
        .then(value => {
            if(value == 'true') get_cart();
        })
        .catch(reason => {log.info(reason)});
}

// 장바구니에 있는 상품들 찜하기
function heart_cart(){
    const body = [];
    const containers = get_clicked_boxes();
    containers.forEach(container => {
        const isbnValue = container.querySelector('.book-info-isbn').value;
        body.push({bookISBN: isbnValue});
    });

    fetch('/user/heart',{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            "X-CSRF-TOKEN": csrfToken
        },
        body: JSON.stringify(body)})
        .then(value => {});
}

//get요청에서 응답받은 데이터로 장바구니 창 생성하기
function create_book_list_in_cart(cartList){
    bookCartContainer.innerHTML = '';
    for(cart of cartList){
        bookCartContainer.insertAdjacentHTML('beforeend',
        `<section class="book-info-container">
                <span><i class="fa-regular fa-circle-check" onclick="checkBox_click(this)"></i></span>
                <input type="hidden" value=${cart.bookISBN} class="book-info-isbn" />
                <img src="" alt="이미지">
                <div class="book-info">
                    <div>${cart.title}</div>
                    <span><b>${cart.price}</b>원</span>
                </div>
                <div class="book-info-price-container">
                    <div><b>${cart.price * cart.bookCount}</b>원</div>
                    <div class="book-info-count-container">
                        <i class="fa-solid fa-minus" onclick="modify_cart(this, '-')"></i>
                        <input type="number" value="${cart.bookCount}">
                        <i class="fa-solid fa-plus" onclick="modify_cart(this, '+')"></i>
                    </div>
                </div>
            </section>`)
    }
}

//모든 체크박스 클릭하는 버튼 이벤트
selectAllBtn.onclick = checkBox_allClick;
//모든 체크박스 클릭하는 로직
function checkBox_allClick(){
    const className = selectAllBtn.className.includes('fa-solid') ? 'fa-regular fa-circle-check' : 'fa-solid fa-circle-check';
    selectAllBtn.className = className;
    [...bookInfoContainer].forEach(container => {
        container.querySelector('i').className = className;
    })
}

//체크박스 하나 클릭하는 로직
function checkBox_click(checkbox){
    if(checkbox.className.includes('fa-solid')){
        checkbox.className = 'fa-regular fa-circle-check';
    }else{
        checkbox.className = "fa-solid fa-circle-check";
    }
}

//선택되어있는 모든 컨테이너 가져오기
function get_clicked_boxes(){
    const containers = [];
    [...bookInfoContainer].forEach(container => {
        const className = container.querySelector('i').className;
        if(className.includes('fa-solid')){
            containers.push(container);
        }
    });
    return containers;
}

//전체 찜하기 버튼 눌렀을 시
heartAllBtn.onclick = heart_cart;

//전체 삭제 버튼 눌렀을 시
removeAllBtn.onclick = delete_cart;


/****************** 결제/주문 **********************/
//주문하기 버튼 눌렀을 시
orderProceedBtn.onclick = () => {
    //주문 총액 가져오기
    const orderTotalPrice = orderInfoContainer.querySelector('.order-total-price').textContent;
    //서버에 넘겨줄 body 값 프레임 생성하기
    const body = {
        paymentVO: {paymentAmount: orderTotalPrice},
        cartVOS: []
    }
    //현재 선택되어있는 ISBN값 가져오기
    const containers = get_clicked_boxes();
    containers.forEach(container => {
        const isbnValue = container.querySelector('.book-info-isbn').value;
        const bookInfoCountValue = container.querySelector('.book-info-count-container').querySelector('input').value;
        body.cartVOS.push({bookISBN: isbnValue, bookCount: bookInfoCountValue});
    });

    if(body.cartVOS.length === 0) {
        alert('상품을 하나라도 선택해주세요!');
    }else{
        fetch('/user/order',{
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                "X-CSRF-TOKEN": csrfToken
            },
            body: JSON.stringify(body)})
            .then(value => value.text())
            .then(value => {location.href = value});
    }

}



