import { search } from './search';

const items = [
    {
        name: '1 к. 19 аудиторія',
        type: {
            description: 'Практична',
        },
        grouped: true,
    },
    {
        name: '1 к. 21 ауд.',
        type: {
            description: 'Лекційна',
        },
        grouped: false,
    },
];

const term = 'аудиторія';
const deepTerm = 'Лекційна';
const grouped = 'групова';
const arr = ['name', 'type.description', grouped];
const excludeTerm = 'exclude9012';

describe('behavior of search function', () => {
    test('shows all items if search term length === 0', () => {
        expect(search(items, '', arr).length).toBe(items.length);
    });

    test('shows items which include search term', () => {
        expect(search(items, term, arr)).toEqual(
            expect.arrayContaining([
                expect.objectContaining({ name: expect.stringContaining(term) }),
            ]),
        );
    });

    test('shows items which include search term equal "групова"', () => {
        expect(search(items, grouped, arr)).toEqual(
            expect.arrayContaining([expect.objectContaining({ grouped: true })]),
        );
    });

    test('it shows items which include search term in deep object', () => {
        expect(search(items, deepTerm, arr)).toEqual(
            expect.arrayContaining([
                expect.objectContaining({
                    type: expect.objectContaining({
                        description: expect.stringContaining(deepTerm),
                    }),
                }),
            ]),
        );
    });

    test('it does not show items which exclude search term', () => {
        expect(search(items, excludeTerm, arr)).toEqual(
            expect.not.arrayContaining([
                expect.objectContaining({ name: expect.stringContaining(excludeTerm) }),
            ]),
        );
    });
});
