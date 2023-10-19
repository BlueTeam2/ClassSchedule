import * as actionsType from './actionsType';

export const addRoomSuccess = (room) => ({
    type: actionsType.ADD_ROOM,
    room,
});

export const addRoomStart = (room) => ({
    type: actionsType.ADD_ROOM_START,
    room,
});

export const deleteRoomSuccess = (roomId, isDisabled) => ({
    type: actionsType.DELETE_ROOM,
    roomId,
    isDisabled,
});

export const deleteRoomStart = (roomId, isDisabled) => ({
    type: actionsType.DELETE_ROOM_START,
    roomId,
    isDisabled,
});

export const getListOfRoomsSuccess = (rooms) => ({
    type: actionsType.SHOW_LIST_OF_ROOMS_SUCCESS,
    rooms,
});

export const getListOfRoomsStart = () => ({
    type: actionsType.GET_LIST_OF_ROOMS_START,
});

export const getListOfDisabledRoomsSuccess = (rooms) => ({
    type: actionsType.SET_DISABLED_ROOMS,
    rooms,
});

export const getListOfDisabledRoomsStart = () => ({
    type: actionsType.GET_LIST_OF_DISABLED_ROOMS_START,
});

export const setSelectRoomSuccess = (roomId) => ({
    type: actionsType.SET_SELECT_ROOM,
    roomId,
});

export const updateRoomSuccess = (room) => ({
    type: actionsType.UPDATE_ROOM,
    room,
});

export const updateRoomStart = (values) => ({
    type: actionsType.UPDATE_ROOM_START,
    values,
});

export const getFreeRoomsStart = (params) => ({
    type: actionsType.GET_FREE_ROOMS_START,
    params,
});

export const getFreeRoomsSuccess = (freeRooms) => ({
    type: actionsType.GET_FREE_ROOMS_SUCCESS,
    freeRooms,
});

export const clearFreeRooms = () => ({
    type: actionsType.CLEAR_FREE_ROOMS,
});

export const getBusyRoomsStart = (semesterId) => ({
    type: actionsType.GET_BUSY_ROOMS_START,
    semesterId,
});

export const getBusyRoomsSuccess = (busyRooms) => ({
    type: actionsType.GET_BUSY_ROOMS_SUCCESS,
    busyRooms,
});
export const clearRoomSuccess = () => ({
    type: actionsType.CLEAR_ROOM,
});

export const handleRoomFormSubmitStart = (values) => ({
    type: actionsType.HANDLE_ROOM_FORM_SUBMIT_START,
    values,
});

export const toggleRoomVisibilityStart = (roomId, isDisabled) => ({
    type: actionsType.TOGGLE_ROOM_VISIBILITY_START,
    roomId,
    isDisabled,
});

export const addRoomTypeSuccess = (roomType) => ({
    type: actionsType.ADD_ROOM_TYPE,
    roomType,
});

export const getAllRoomTypesSuccess = (roomType) => ({
    type: actionsType.GET_ALL_ROOM_TYPES,
    roomType,
});

export const getAllRoomTypesStart = () => ({
    type: actionsType.GET_ALL_ROOM_TYPES_START,
});

export const deleteRoomTypeSuccess = (roomTypeId) => ({
    type: actionsType.DELETE_ROOM_TYPE,
    roomTypeId,
});

export const deleteRoomTypeStart = (roomTypeId) => ({
    type: actionsType.DELETE_ROOM_TYPE_START,
    roomTypeId,
});

export const updateRoomTypeSuccess = (roomType) => ({
    type: actionsType.UPDATE_ROOM_TYPE,
    roomType,
});

export const selectRoomType = (typeId) => ({
    type: actionsType.SELECT_ROOM_TYPE,
    typeId,
});

export const handleRoomTypeFormSubmitStart = (values) => ({
    type: actionsType.HANDLE_ROOM_TYPE_FORM_SUBMIT_START,
    values,
});
