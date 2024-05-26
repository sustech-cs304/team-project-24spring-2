
import axios from 'axios';

function logout() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('token_type');
    localStorage.removeItem('expire_time');
    localStorage.removeItem('uuid');
    localStorage.removeItem('permission_group');
}

async function verifyLoginStateWithAccess() : Promise<boolean> {
    if (!verifyLoginState()) {
        return false;
    }
    let response = await axios.post('/api/user/verify', {}, {
        headers: {
            'Authorization': localStorage.getItem('token_type') + ' ' + localStorage.getItem('access_token')
        }
    });
    if (response.status === 200) {
        return true;
    }
    localStorage.removeItem('access_token');
    localStorage.removeItem('token_type');
    localStorage.removeItem('expire_time');
    localStorage.removeItem('uuid');
    return false;
}

function verifyLoginState() : boolean {
    if (!localStorage.getItem('access_token') || !localStorage.getItem('token_type')) {
        return false;
    }
    if (!localStorage.getItem('expire_time') || new Date().getTime() > parseInt(localStorage.getItem('expire_time') as string)) {
        return false;
    }
    return true;
}

export default { verifyLoginState, verifyLoginStateWithAccess, logout };