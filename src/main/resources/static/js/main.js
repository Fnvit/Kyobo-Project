const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute("content");
const bookResultContainer = document.getElementById('book-result-container');
const heartAllBtn = document.getElementById('heart-all-btn');
const cartAllBtn = document.getElementById('cart-all-btn');

//처음 접속 시 책 리스트를 생성함
get_books();

// 책 찜하기/장바구니 넣기 공통 처리 부분
// (버튼 눌렀을 시 => 데이터 생성, 가능여부 체크)
function insert_heart_cart_checked(userEmail, clickedBtn){
    console.log(userEmail);
    // 로그인이 안되어있다
    if(!userEmail){
        alert('로그인부터 하세요^-^');
    }else{
        const bookISBNArray = [];
        const bookInfoContainer = document.getElementsByClassName('book-info-container');
        for(container of bookInfoContainer){
            const bookInfoCheck = container.getElementsByClassName('book-info-check').item(0);
            //체크박스가 선택되어 있다면
            if(bookInfoCheck.checked){
                const bookInfoISBN = container.getElementsByClassName('book-info-isbn').item(0);
                bookISBNArray.push({ userEmail: userEmail, bookISBN : bookInfoISBN.value });
            }
        }
        //내가 넣은 배열에 아무것도 들어있지 않다면
        if(bookISBNArray.length === 0){
            alert('상품을 하나라도 선택해주세요!');
        }else{
            insert(clickedBtn, bookISBNArray);
        }
    }
}

// 책을 장바구니에 넣거나 / 혹은 찜 하는 메소드
function insert(clickedBtn, bookISBNArray){
    const requestURL = clickedBtn === heartAllBtn ? '/main/heart' : '/main/cart';
    fetch(requestURL, {
            method: 'POST',
            headers: {
                "content-Type": 'application/json',
                "X-CSRF-TOKEN": csrfToken
            },
            body: JSON.stringify(bookISBNArray)
        })
        .then(value => value.text())
        .then(value => { console.log(value); })
        .catch(reason => { console.log(reason); });
}

// 모든 책의 정보를 가져오는 메소드
function get_books(){
    fetch('/main/books')
        .then(response => response.json())
        .then(value => { create_book_list(value); })
        .catch();
}

//　책 리스트를 생성하는 메소드
function create_book_list(bookList){
    bookResultContainer.innerHTML = '';
    for(book of bookList){
        bookResultContainer.insertAdjacentHTML('beforeend',
            '<div class="book-info-container">\n' +
            `<input type="hidden" class="book-info-isbn" value="${book.isbn}">` +
            '                <span><input class="book-info-check" type="checkbox"></span>\n' +
            '                <img src="" alt="상품이미지">\n' +
            `                <a href="#" class="book-info-title">${book.title}</a>\n` +
            '                <span>\n' +
            `                    <a href="#">${book.author}</a>\n` +
            '                     *\n' +
            `                    <a href="#">${book.publisher}</a>\n` +
            '                </span>\n' +
            `                <span>${book.price}원</span>\n` +
            '                <span><input class="book-info-heart" type="button" value="하트"></span>\n' +
            '            </div>');
    }
}







