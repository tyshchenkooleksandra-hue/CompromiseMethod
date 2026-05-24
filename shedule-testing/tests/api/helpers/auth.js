const axios = require('axios');

const BASE_URL = 'http://localhost:8080';

async function getAuthToken() {

    console.log('\n========== AUTH DEBUG ==========');

    console.log('BASE_URL:', BASE_URL);

    const requestData = {
        email: 'manager@gmail.com',
        password: 'Qwerty!123',
    };

    console.log('REQUEST URL:',
        `${BASE_URL}/auth/sign-in`);

    console.log('REQUEST BODY:',
        JSON.stringify(requestData, null, 2));

    try {

        const response = await axios({
            method: 'post',
            url: `${BASE_URL}/auth/sign-in`,
            data: requestData,
            validateStatus: () => true
        });

        console.log('\n----- RESPONSE -----');

        console.log('STATUS:',
            response.status);

        console.log('HEADERS:',
            response.headers);

        console.log('DATA:',
            JSON.stringify(response.data, null, 2));

        console.log('===============================\n');

        return response.data.token;

    } catch (error) {

        console.log('\n----- ERROR -----');

        console.log('MESSAGE:',
            error.message);

        console.log('CODE:',
            error.code);

        console.log('RESPONSE STATUS:',
            error.response?.status);

        console.log('RESPONSE DATA:',
            error.response?.data);

        console.log('FULL ERROR:',
            error);

        console.log('=================\n');

        throw error;
    }
}

module.exports = {
    getAuthToken,
    BASE_URL
};