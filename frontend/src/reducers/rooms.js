import * as actionTypes from '../actions/actionsType';

const initialState = {
    rooms: [],
    disabledRooms: [],
    roomTypes: [],
    oneRoom: {},
    oneType: {},
    freeRooms: [],
};
const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.ADD_ROOM:
            return {
                ...state,
                rooms: [action.room, ...state.rooms],
                oneRoom: {},
            };

        case actionTypes.DELETE_ROOM:
            if (action.isDisabled) {
                return {
                    ...state,
                    disabledRooms: [
                        ...state.disabledRooms.filter((room) => room.id !== action.roomId),
                    ],
                };
            }
            return {
                ...state,
                rooms: [...state.rooms.filter((room) => room.id !== action.roomId)],
            };

        case actionTypes.SHOW_LIST_OF_ROOMS_SUCCESS:
            return { ...state, rooms: [...action.rooms] };

        case actionTypes.SET_DISABLED_ROOMS:
            return { ...state, disabledRooms: [...action.rooms] };

        case actionTypes.SET_SELECT_ROOM: {
            const room = state.rooms.find((roomItem) => roomItem.id === action.roomId);
            return { ...state, oneRoom: room };
        }

        case actionTypes.UPDATE_ROOM: {
            const rooms = [...state.rooms];
            rooms[rooms.findIndex((roomItem) => roomItem.id === action.room.id)] = action.room;
            return { ...state, oneRoom: {}, rooms: [...rooms] };
        }

        case actionTypes.CLEAR_ROOM:
            return { ...state, oneRoom: {} };

        case actionTypes.ADD_ROOM_TYPE:
            return { ...state, roomTypes: [...state.roomTypes, action.roomType] };

        case actionTypes.GET_ALL_ROOM_TYPES:
            return { ...state, roomTypes: [...action.roomType] };

        case actionTypes.DELETE_ROOM_TYPE:
            return {
                ...state,
                roomTypes: [...state.roomTypes.filter((type) => type.id !== action.roomTypeId)],
            };

        case actionTypes.UPDATE_ROOM_TYPE: {
            const roomTypes = [...state.roomTypes];
            roomTypes[roomTypes.findIndex((typeItem) => typeItem.id === action.roomType.id)] =
                action.roomType;
            return { ...state, oneType: {}, roomTypes: [...roomTypes] };
        }

        case actionTypes.SELECT_ROOM_TYPE: {
            const type = state.roomTypes.find((roomType) => roomType.id === action.typeId);
            return { ...state, oneType: type };
        }

        case actionTypes.GET_FREE_ROOMS_SUCCESS: {
            return { ...state, freeRooms: action.freeRooms };
        }
        case actionTypes.GET_BUSY_ROOMS_SUCCESS: {
            return { ...state, rooms: action.busyRooms };
        }
        case actionTypes.CLEAR_FREE_ROOMS: {
            return { ...state, freeRooms: [] };
        }
        default:
            return state;
    }
};

export default reducer;
