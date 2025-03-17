const LOGIN_USER_KEY = 'login_user_key';

function save(token){
    localStorage.setItem(LOGIN_USER_KEY,token);
}

function get(){
    return localStorage.getItem(LOGIN_USER_KEY);
}

function remove(){
    localStorage.removeItem(LOGIN_USER_KEY);
}

export {save,get,remove};