export const handleFormSubmit = (values, addItem, updateItem) => {
    if (values.id) {
        return updateItem;
    }
    return addItem;
};
