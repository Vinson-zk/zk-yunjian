/**
 *
 * @Author: 
 * @Date: 
 * @Last 
 * @Last 
 */

import React, { Component } from 'react';
//  import { connect } from 'dva';
import { injectIntl } from 'react-intl';

import { zkTools, ZKCustomComponents, ZKOriginalComponents } from "zkFramework";        
const {
} = ZKOriginalComponents;

const { 
	ZKSearchRow, 	
} = ZKCustomComponents;

const { zkToolsMsg } = zkTools;
const ZKSearchItem = ZKSearchRow.Item;

class CInitGenTestModuleSubTestTableGenSearch extends React.Component {

    constructor(props) {
        super(props);
        this.state = {}
    }

    f_search = filter=>{
        let { dispatch, mGenTestModuleSubTestTableGen } = this.props;
        if(!filter){
            filter = mGenTestModuleSubTestTableGen.initFilter;
        }
        dispatch({ 
            type: "mGenTestModuleSubTestTableGen/findGenTestModuleSubTestTableGens", 
            filter: {...mGenTestModuleSubTestTableGen.filter, ...filter}, 
            callback: e => { } 
        });
    }
    
    render(){
        let { intl, mApp, mGenTestModuleSubTestTableGen, locales, loading } = this.props;
        let lang = mApp.lang?mApp.lang:zkToolsMsg.getLocale();

        return (
            <ZKSearchRow 
                initialValues={mGenTestModuleSubTestTableGen.initFilter} 
                filter={mGenTestModuleSubTestTableGen.filter||mGenTestModuleSubTestTableGen.initFilter} 
                resetFunc={values => {}}
                searchFunc={values => {
                    this.f_search.call(this, values);
                }}
            >
            </ZKSearchRow>
        );
    }
}

export default CInitGenTestModuleSubTestTableGenSearch;