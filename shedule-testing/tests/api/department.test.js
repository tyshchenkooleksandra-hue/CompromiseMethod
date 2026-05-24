require('dotenv').config();

const axios = require('axios');
const { BASE_URL, getAuthToken } = require('./helpers/auth');

describe('Departments API', () => {

    let headers;
    let createdDepartmentId;

    beforeAll(async () => {

        const token = await getAuthToken();

        headers = {
            Authorization: `Bearer_${token}`,
            'Content-Type': 'application/json',
        };

    });

    // ─────────────────────────────────────────────
    // CRUD cycle
    // ─────────────────────────────────────────────

    describe('CRUD cycle', () => {

        const departmentName =
            `QA_Department_${Date.now()}`;

        // ── 1 CREATE ───────────────────────────
        test('POST /departments — creates department', async () => {

            const res = await axios.post(
                `${BASE_URL}/departments`,
                {
                    name: departmentName
                },
                { headers }
            );

            expect(res.status)
                .toBe(201);

            expect(res.data)
                .toHaveProperty('id');

            expect(res.data.name)
                .toBe(departmentName);

            createdDepartmentId =
                res.data.id;

        });

        // ── 2 GET ALL ──────────────────────────
        test('GET /departments — returns 200 and array', async () => {

            const res = await axios.get(
                `${BASE_URL}/departments`,
                { headers }
            );

            expect(res.status)
                .toBe(200);

            expect(Array.isArray(res.data))
                .toBe(true);

        });

        // ── 3 GET BY ID ────────────────────────
        test('GET /departments/:id — returns created department', async () => {

            const res = await axios.get(
                `${BASE_URL}/departments/${createdDepartmentId}`,
                { headers }
            );

            expect(res.status)
                .toBe(200);

            expect(res.data.id)
                .toBe(createdDepartmentId);

            expect(res.data.name)
                .toBe(departmentName);

        });

        // ── 4 UPDATE ───────────────────────────
        test('PUT /departments — updates department', async () => {

            const updatedName =
                `Updated_Department_${Date.now()}`;

            const res = await axios.put(
                `${BASE_URL}/departments`,
                {
                    id: createdDepartmentId,
                    name: updatedName
                },
                { headers }
            );

            expect([200, 204])
                .toContain(res.status);

        });

        // ── 5 DELETE ───────────────────────────
        test('DELETE /departments/:id — deletes department', async () => {

            const res = await axios.delete(
                `${BASE_URL}/departments/${createdDepartmentId}`,
                { headers }
            );

            expect([200, 204])
                .toContain(res.status);

        });

        // ── 6 VERIFY 404 AFTER DELETE ──────────
        test('GET deleted department — returns 404', async () => {

            await expect(
                axios.get(
                    `${BASE_URL}/departments/${createdDepartmentId}`,
                    { headers }
                )
            ).rejects.toMatchObject({
                response: {
                    status: 404
                }
            });

        });

    });

    // ─────────────────────────────────────────────
    // Negative scenarios
    // ─────────────────────────────────────────────

    describe('Negative scenarios', () => {

        // ── 7 INVALID ID ───────────────────────
        test('GET /departments/999999 — returns 404', async () => {

            await expect(
                axios.get(
                    `${BASE_URL}/departments/999999`,
                    { headers }
                )
            ).rejects.toMatchObject({
                response: {
                    status: 404
                }
            });

        });

        // ── 8 INVALID BODY ─────────────────────
        test('POST /departments with empty name — returns 400', async () => {

            await expect(
                axios.post(
                    `${BASE_URL}/departments`,
                    {
                        name: ''
                    },
                    { headers }
                )
            ).rejects.toMatchObject({
                response: {
                    status: 400
                }
            });

        });

        // ── 9 WITHOUT TOKEN ────────────────────
        test('GET /departments without token — returns public access', async () => {

            const res = await axios.get(
                `${BASE_URL}/departments`
            );

            expect(res.status)
                .toBe(200);

            expect(Array.isArray(res.data))
                .toBe(true);

        });

    });

});
