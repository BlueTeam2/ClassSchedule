import { handleFormSubmit } from './handleFormSubmit';

describe('handleFormSubmit function', () => {
    it('should return updateItem if the values are id', () => {
        const values = { id: 1 };
        const updateItem = { name: 'test' };
        expect(handleFormSubmit(values, {}, updateItem)).toEqual(updateItem);
    });
    it('should return addItem if the values is empty', () => {
        const values = {};
        const addItem = { name: 'test' };
        expect(handleFormSubmit(values, addItem, {})).toEqual(addItem);
    });
});
