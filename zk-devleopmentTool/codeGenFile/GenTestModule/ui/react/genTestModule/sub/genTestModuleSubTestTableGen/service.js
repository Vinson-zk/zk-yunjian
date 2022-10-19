/**
 *
 * @Author: 
 * @Date: 
 * @Last Modified by:   
 * @Last Modified time: 
 */

import { zkTools } from "zkFramework";
const { zkToolsAjax } = zkTools;

const api = globalAppConfig.apiPrefixGenTestModule;
// const api = "apiMock";
/*
JSON.stringify(params)
contentType:'application/json; charset=utf-8'}
*/
// 编辑 
export async function editGenTestModuleSubTestTableGen(params) {
    return zkToolsAjax.reqPretreatment(
        `/${api}/sub/genTestModuleSubTestTableGen/genTestModuleSubTestTableGen`, 
        {method:'POST', data:JSON.stringify(params), contentType:'application/json; charset=utf-8'},
        res=>{
            let filterCodes = ["zk.000002"];
            if(filterCodes.includes(res.code)){
                return true;
            } 
            return false
        }
        );
}

// 删除
export async function delGenTestModuleSubTestTableGen(params) {
    return zkToolsAjax.reqPretreatment(`/${api}/sub/genTestModuleSubTestTableGen/genTestModuleSubTestTableGen`, {method:'DELETE', data:params});
}

// 查询 详情
export async function getGenTestModuleSubTestTableGen(params) {
	return zkToolsAjax.reqPretreatment(`/${api}/sub/genTestModuleSubTestTableGen/genTestModuleSubTestTableGen`, {method:'GET', data:params});
}

// 查询 分页列表
export async function findGenTestModuleSubTestTableGens(params) {
	return zkToolsAjax.reqPretreatment(`/${api}/sub/genTestModuleSubTestTableGen/genTestModuleSubTestTableGensPage`, {method:'GET', data:params});
}