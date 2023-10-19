import '@testing-library/jest-dom/extend-expect';
import EnzymeAdapter from 'enzyme-adapter-react-16';
import Enzyme, { shallow, render, mount } from 'enzyme';

Enzyme.configure({ adapter: new EnzymeAdapter() });

global.shallow = shallow;
global.render = render;
global.mount = mount;

// Fail tests on any warning
console.error = (message) => {
    throw new Error(message);
};
