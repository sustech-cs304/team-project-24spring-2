import Mock from 'mockjs';
import setupMock, { successResponseWrap } from '@/utils/setup-mock';

setupMock({
    setup() {
      // submit
      Mock.mock(new RegExp('/api/event/create-event'), () => {
        return successResponseWrap('ok');
      });
    },
  });