import { isEmpty } from 'lodash';
import { GROUPED } from '../constants/common';

export const search = (items, term, arr) => {
    const termTmp = term.trim();
    if (isEmpty(termTmp)) return items;

    return items.filter((item) => {
        if (item.grouped && GROUPED.includes(termTmp.toLowerCase())) return true;

        for (let i = 0; i < arr.length; i += 1) {
            let data = item[arr[i]];

            if (arr[i].includes('.')) {
                const [objectName, property] = arr[i].split('.');
                data = item[objectName]?.[property];
            }
            if (String(data).toLowerCase().indexOf(termTmp.toLowerCase()) > -1) return true;
        }
        return false;
    });
};
