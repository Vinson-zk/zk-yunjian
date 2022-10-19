/***  功能引用定义/function；集成功能 js 中使用；***/ 
import cGenTestModuleSubTestTableGenIndex from "./index.js";
import mGenTestModuleSubTestTableGen from "./model.js";
import cGenTestModuleSubTestTableGenDetail from "./detail.js";
import cGenTestModuleSubTestTableGenEdit from "./edit.js";

const genTestModuleSubTestTableGenIndex = { onEnter: undefined, component: cGenTestModuleSubTestTableGenIndex, models: [mGenTestModuleSubTestTableGen] };
const genTestModuleSubTestTableGenDetail = { onEnter: undefined, component: cGenTestModuleSubTestTableGenDetail, models: [mGenTestModuleSubTestTableGen] };
const genTestModuleSubTestTableGenEdit = { onEnter: undefined, component: cGenTestModuleSubTestTableGenEdit, models: [mGenTestModuleSubTestTableGen] };

export {
	genTestModuleSubTestTableGenIndex, genTestModuleSubTestTableGenDetail, genTestModuleSubTestTableGenEdit,
}