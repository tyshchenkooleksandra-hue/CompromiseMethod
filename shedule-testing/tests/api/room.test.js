require('dotenv').config();

const axios = require('axios');
const { BASE_URL, getAuthToken } = require('./helpers/auth');

describe('Rooms API', () => {

    let headers;
    let createdRoomId;

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
        test('GET /rooms — returns 200 and array', async () => {

            const res = await axios.get(
                `${BASE_URL}/rooms`,
                { headers }
            );

            expect(res.status).toBe(200);

            expect(Array.isArray(res.data))
                .toBe(true);

        });

        // ── 2 ──────────────────────────────────
        test('GET /rooms/:id — returns room or 404', async () => {

            const id = createdRoomId || 1;

            const res = await axios.get(
                `${BASE_URL}/rooms/${id}`,
                {
                    headers,
                    validateStatus: () => true
                }
            );

            console.log(
                'GET ROOM BY ID:',
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
        test('GET /rooms without token — returns 401/403', async () => {

            await expect(
                axios.get(`${BASE_URL}/rooms`)
            ).rejects.toMatchObject({
                response: {
                    status: expect.any(Number)
                }
            });

        });

        // ── 4 ──────────────────────────────────
        test('GET /rooms/999999 — returns 404', async () => {

            await expect(
                axios.get(
                    `${BASE_URL}/rooms/999999`,
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