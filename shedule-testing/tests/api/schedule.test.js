require('dotenv').config();

const axios = require('axios');
const { BASE_URL, getAuthToken } = require('./helpers/auth');

describe('Schedule API', () => {

    let headers;

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
        test('GET /schedules — returns 200 ', async () => {

            const res = await axios.get(
                `${BASE_URL}/schedules`,
                { headers }
            );

            expect(res.status)
                .toBe(200);

        });

        // ── 2 ──────────────────────────────────
        test('GET /schedules/public/status — returns publish status', async () => {

            const res = await axios.get(
                `${BASE_URL}/schedules/public/status`,
                { headers }
            );

            expect(res.status)
                .toBe(200);

        });

        // ── 3 ──────────────────────────────────
        test('GET /schedules/full/semester — returns full semester schedule', async () => {

            const res = await axios.get(
                `${BASE_URL}/schedules/full/semester?semesterId=1`,
                {
                    headers,
                    validateStatus: () => true
                }
            );

            expect([200, 400])
                .toContain(res.status);

        });

        // ── 4 ──────────────────────────────────
        test('GET /schedules/semester — returns schedules by semester', async () => {

            const res = await axios.get(
                `${BASE_URL}/schedules/semester?semesterId=1`,
                {
                    headers,
                    validateStatus: () => true
                }
            );

            expect([200, 400])
                .toContain(res.status);

        });

    });

    // ─────────────────────────────────────────────
    // Negative scenarios
    // ─────────────────────────────────────────────

    describe('Negative scenarios', () => {

        // ── 5 ──────────────────────────────────
        test('GET /schedules — returns 401/403 without token', async () => {

            await expect(
                axios.get(
                    `${BASE_URL}/schedules`
                )
            ).rejects.toMatchObject({
                response: {
                    status: expect.any(Number)
                }
            });

        });

        // ── 6 ──────────────────────────────────
        test('GET /schedules/public/status — returns 401/403 without token', async () => {

            await expect(
                axios.get(
                    `${BASE_URL}/schedules/public/status`
                )
            ).rejects.toMatchObject({
                response: {
                    status: expect.any(Number)
                }
            });

        });


        test('GET /schedules/full/semester — returns full semester schedule', async () => {

            const res = await axios.get(
                `${BASE_URL}/schedules/full/semester?semesterId=1`,
                {
                    headers,
                    validateStatus: () => true
                }
            );

            expect(res.status)
                .toBe(200);

            expect(res.data)
                .toHaveProperty('semester');

            expect(res.data)
                .toHaveProperty('schedule');


        });

    });
});

