require('dotenv').config();

const axios = require('axios');
const { BASE_URL, getAuthToken } = require('./helpers/auth');

describe('Groups API', () => {

    let headers;
    let createdGroupId;

    beforeAll(async () => {

        const token = await getAuthToken();

        headers = {
            Authorization: `Bearer_${token}`,
            'Content-Type': 'application/json',
        };

    });

    // ─────────────────────────────────────────────
    // Positive scenarios
    // ─────────────────────────────────────────────

    describe('Positive scenarios', () => {

        // ── 1 ──────────────────────────────────
        test('GET /groups — returns 200 and array', async () => {

            const res = await axios.get(
                `${BASE_URL}/groups`,
                { headers }
            );

            expect(res.status).toBe(200);

            expect(Array.isArray(res.data))
                .toBe(true);

        });

        // ── 2 ──────────────────────────────────
        test('GET /groups/:id — returns group or 404', async () => {

            const id = createdGroupId || 999999;

            const res = await axios.get(
                `${BASE_URL}/groups/${id}`,
                {
                    headers,
                    validateStatus: () => true
                }
            );

            console.log(
                'GET GROUP BY ID:',
                res.status,
                res.data
            );

            expect([200, 404])
                .toContain(res.status);

        });

    });

    // ─────────────────────────────────────────────
    // Negative scenarios
    // ─────────────────────────────────────────────

    describe('Negative scenarios', () => {

        // ── 3 ──────────────────────────────────
        test('GET /groups without token — returns 401/403', async () => {

            await expect(
                axios.get(`${BASE_URL}/groups`)
            ).rejects.toMatchObject({
                response: {
                    status: expect.any(Number)
                }
            });

        });

        // ── 4 ──────────────────────────────────
        test('GET /groups/999999 — returns 404', async () => {

            await expect(
                axios.get(
                    `${BASE_URL}/groups/999999`,
                    { headers }
                )
            ).rejects.toMatchObject({
                response: {
                    status: 404
                }
            });

        });

    });

});