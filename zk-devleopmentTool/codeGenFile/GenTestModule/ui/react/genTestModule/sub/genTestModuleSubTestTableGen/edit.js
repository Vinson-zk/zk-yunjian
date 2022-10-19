/**
 *
 * @Author: 
 * @Date: 
 * @Last 
 * @Last 
 */

import React, { Component } from 'react';
import { injectIntl } from 'react-intl';
import { connect } from 'dva';
import { Icon } from "antd";

import locales from "../../../locales/index";
import { zkTools, ZKCustomComponents, ZKOriginalComponents } from "zkFramework";
// ZKOriginalComponents
const { ZKSpin, ZKModal,ZKInput, ZKInputNumber,} = ZKOriginalComponents;
// ZKCustomComponents
const { ZKEditForm, ZKInputJson,} = ZKCustomComponents;
// zkTools
const { zkToolsMsg, zkToolsValidates, zkToolsNavAndMenu } = zkTools;  

class CInitGenTestModuleSubTestTableGenEdit extends Component {

    formRef = React.createRef();

    // 1、构造函数
    constructor(props) {
        super(props);
        this.state = {
            loading: false,
        }
    };
	
	/** 保存 */
    f_save = (values, form, callbackFunc) => {
        this.props.dispatch({
            type: 'mGenTestModuleSubTestTableGen/editGenTestModuleSubTestTableGen', 
            payload: values, 
            callback: (errors) => {
                if(!errors){
                    this.setState({loading: true});
                }
                callbackFunc(errors);
            }
        });
    };

    /** 返回 JSX 元素 */
    render() {

        let { location, mApp, dispatch, mGenTestModuleSubTestTableGen, intl, loading } = this.props;
        let { optEntity } = mGenTestModuleSubTestTableGen;
        
		let lang = mApp.lang?mApp.lang:zkToolsMsg.getLocale();


        // ZKJson 自定义校验规则对象
        let f_makeObjRuls = required=>{
            let objRule = {};
            for(let index in locales){
                objRule[index] = zkToolsValidates.string(intl, 1, 64, required);
            }
            return objRule;
        }
        
        let spinning = this.state.loading || loading.effects['mGenTestModuleSubTestTableGen/editGenTestModuleSubTestTableGen'] || loading.effects['mGenTestModuleSubTestTableGen/getGenTestModuleSubTestTableGen'];
        return (optEntity != null && mGenTestModuleSubTestTableGen.pathname == location.pathname) && (
            <ZKSpin spinning={spinning === true} >
                <ZKEditForm ref = {this.formRef} history={history} data={optEntity}
                    saveFunc={this.f_save}
                    resetFunc={form => { return true; }}
                >
                	<ZKEditForm.Item name = "testa" label = {zkToolsMsg.msgFormatByIntl(intl, 'zk.genTestModule.sub.GenTestModuleSubTestTableGen.testa')} 
                		rules = {[
							zkToolsValidates.string(intl, 1, 63, true), 
                        ]} 
					>
                        <ZKInput placeholder={zkToolsMsg.msgFormatByIntl(intl, 'zk.genTestModule.sub.GenTestModuleSubTestTableGen.testa.placeholder')}
                        	disabled = {optEntity.pkId?true:false} />
                	</ZKEditForm.Item>
                	<ZKEditForm.Item name = "testb" label = {zkToolsMsg.msgFormatByIntl(intl, 'zk.genTestModule.sub.GenTestModuleSubTestTableGen.testb')} 
                		rules = {[
							zkToolsValidates.integer(intl, 0, 999999999, true), 
                        ]} 
					>
                        <ZKInputNumber placeholder={zkToolsMsg.msgFormatByIntl(intl, 'zk.genTestModule.sub.GenTestModuleSubTestTableGen.testb.placeholder')}
                        	precision={0} disabled = {optEntity.pkId?true:false} />
                	</ZKEditForm.Item>
                	<ZKEditForm.Item name = "testc" label = {zkToolsMsg.msgFormatByIntl(intl, 'zk.genTestModule.sub.GenTestModuleSubTestTableGen.testc')} 
                		rules = {[
							zkToolsValidates.integer(intl, 0, 999999999, true), 
                        ]} 
					>
                        <ZKInputNumber placeholder={zkToolsMsg.msgFormatByIntl(intl, 'zk.genTestModule.sub.GenTestModuleSubTestTableGen.testc.placeholder')}
                        	precision={0} disabled = {optEntity.pkId?true:false} />
                	</ZKEditForm.Item>
                	<ZKEditForm.Item name = "testJson" label = {zkToolsMsg.msgFormatByIntl(intl, 'zk.genTestModule.sub.GenTestModuleSubTestTableGen.testJson')} 
                		rules = {[
							zkToolsValidates.object(intl, locales, undefined, f_makeObjRuls(true), true), 
                        ]} 
					>
                        <ZKInputJson placeholder={zkToolsMsg.msgFormatByIntl(intl, 'zk.genTestModule.sub.GenTestModuleSubTestTableGen.testJson.placeholder')}
                        	styleType="compact" primaryAttr={lang} attrs={locales} />
                	</ZKEditForm.Item>
                	<ZKEditForm.Item name = "testJson2" label = {zkToolsMsg.msgFormatByIntl(intl, 'zk.genTestModule.sub.GenTestModuleSubTestTableGen.testJson2')} 
                		rules = {[
							zkToolsValidates.object(intl, locales, undefined, f_makeObjRuls(true), true), 
                        ]} 
					>
                        <ZKInputJson placeholder={zkToolsMsg.msgFormatByIntl(intl, 'zk.genTestModule.sub.GenTestModuleSubTestTableGen.testJson2.placeholder')}
                        	styleType="compact" primaryAttr={lang} attrs={locales} />
                	</ZKEditForm.Item>
            	</ZKEditForm>
            </ZKSpin>
        )
    }

    // 6、创建时；安装组件（插入树中）后立即调用；此方法是设置任何订阅的好地方。如果您这样做，请不要忘记取消订阅componentWillUnmount()。
    componentDidMount() {
        let { location, match, dispatch, mGenTestModuleSubTestTableGen } = this.props;
        let { params } = match;
        if (mGenTestModuleSubTestTableGen.pathname != location.pathname) {
        	// 第一次进来或地址栏改变了
            dispatch({ type: 'mGenTestModuleSubTestTableGen/setState', payload: { pathname: location.pathname, optEntity: undefined } });
            if("_new" == params.pkId){
            	dispatch({ type: 'mGenTestModuleSubTestTableGen/setState', payload: { optEntity: {} } });
            }else{
            	dispatch({ type: 'mGenTestModuleSubTestTableGen/getGenTestModuleSubTestTableGen', payload: { pkId: params.pkId } });
            }
        }
    }

    // 6、修改时；更新发生后立即调用。初始渲染不会调用此方法。
    componentDidUpdate(prevProps, prevState, snapshot) {

    }

    // 卸载时；在卸载和销毁组件之前立即调用。在此方法中执行任何必要的清理，例如使计时器无效，取消网络请求或清除在其中创建的任何订阅
    componentWillUnmount() {
		// let { mGenTestModuleSubTestTableGen, dvaApp } = this.props;
		// zkToolsNavAndMenu.unRegisterModel(dvaApp, [mGenTestModuleSubTestTableGen]);
    }

}

// {onValuesChange:(props, changedValues, allValues) =>{
// 	console.log("--- ", props, changedValues, allValues)
// }}

export default injectIntl(connect(({ mApp, mGenTestModuleSubTestTableGen, loading }) => ({ mApp, mGenTestModuleSubTestTableGen, loading }))(CInitGenTestModuleSubTestTableGenEdit));