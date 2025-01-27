import { useMemo } from 'react';
import { usePlatformStyles } from '@/hooks/usePlatformStyles';

export const usePickerStyles = () => {
    const { platform } = usePlatformStyles();

    const PickerStyles = useMemo(() => {
        if (!platform) {
            return {
                pickSelection: {
                    flex: 1,
                    flexDirection: 'column',
                },
                picker: {
                    flex: 1,
                    borderColor: 'gray',
                    borderWidth: 1,
                    paddingLeft: 2,
                    marginRight: 20,
                    marginLeft: 10,
                    height: 30,
                },
            };
        }

        return {
            pickSelection: {
                flex: 1,
                flexDirection: platform === 'web' ? "row" : "column",
            },
            picker: {
                flex: 1,
                borderColor: 'gray',
                borderWidth: 1,
                paddingLeft: 2,
                marginRight: 20,
                marginLeft: 10,
                height: 30,
            },
            iosPicker: {
                inputIOS: {
                    height: 40,
                    borderColor: 'gray',
                    borderWidth: 1,
                    borderRadius: 4,
                    paddingLeft: 10,
                    marginRight: 20,
                    marginLeft: 10,
                    marginBottom: 20,
                },
                inputIOSContainer: {
                    flex: 1,
                    backgroundColor: 'white',
                    borderRadius: 4,
                },
            },
            androidPicker: {
                inputAndroid: {
                    height: 40,
                    borderColor: 'gray',
                    borderWidth: 1,
                    borderRadius: 4,
                    paddingLeft: 10,
                    marginRight: 20,
                    marginLeft: 10,
                },
                inputAndroidContainer: {
                    flex: 1,
                },
            },
            webPicker: {
                inputWeb: {
                    flex: 1,
                    borderColor: 'gray',
                    borderWidth: 1,
                    paddingLeft: 2,
                    marginRight: 20,
                    marginLeft: 10,
                    height: 30,
                },
                inputWebContainer: {
                    flex: 1,
                },
            },
        };
    }, [platform]);

    return PickerStyles;
};