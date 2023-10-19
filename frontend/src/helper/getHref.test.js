import { shallow } from 'enzyme';
import { getHref } from './getHref';

describe('getHref function', () => {
    it('should return link with href', () => {
        const link = 'https://www.youtube.com/';
        const wrapper = shallow(getHref(link));
        expect(wrapper.exists()).toBeTruthy();
        expect(wrapper.props().href).toEqual(link);
        expect(wrapper.props().title).toEqual(link);
    });
});
