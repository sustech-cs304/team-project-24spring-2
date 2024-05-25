
import axios from 'axios';

function logout() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('token_type');
    localStorage.removeItem('expire_time');
    localStorage.removeItem('uuid');
    localStorage.removeItem('permission_group');
}

function verifyLoginState(access : boolean) : boolean {
    if (!localStorage.getItem('access_token') || !localStorage.getItem('token_type')) {
        return false;
    }
    if (!localStorage.getItem('expire_time') || new Date().getTime() > parseInt(localStorage.getItem('expire_time') as string)) {
        return false;
    }
    if (access) {
        let flag = false;
        axios.post('/api/user/verify/', {}, {
            headers: {
                'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
            }
        }).then(res => {
            if (res.status !== 200) {
                return;
            }
            flag = true;
        }).catch(err => {});
        if (!flag) {
            localStorage.removeItem('access_token');
            localStorage.removeItem('token_type');
            localStorage.removeItem('expire_time');
            localStorage.removeItem('uuid');
        }
        return flag;
    }
    return true;
}

export default { verifyLoginState, logout };