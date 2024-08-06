/*
* @Author: Vinson
* @Date:   2020-01-10 17:28:55
* @Last Modified by:   Vinson
* @Last Modified time: 2020-02-29 22:14:02
* 
* 
* 
*/

(function(_zk){

	/*** AES 加解密 ***/
	var AES = {
	    ivKey:'ihaierForTodo_Iv', // 要与后台相同
	    /**
	     * @hexKey: 密码使用的 hex 编码；
	     * @return: 返回密文使用 hex 编码；
	     */
	    encrypt: function(content, hexKey, ivKey) {
	        var _key = CryptoJS.enc.Hex.parse(hexKey);
	        var _iv = CryptoJS.enc.Utf8.parse(ivKey);
	        content = CryptoJS.enc.Utf8.parse(content);
	        content = CryptoJS.AES.encrypt(content, _key, {
	            iv: _iv,
	            mode: CryptoJS.mode.CBC,
	            padding: CryptoJS.pad.Pkcs7
	        });
	        // console.log("[^_^:20200110-1512-001] eStr: ", eStr); 
	        return content.ciphertext.toString(CryptoJS.enc.Hex);
	    },
	    /**
	     * @eContent: 待解密密文使用 hex 编码；
	     * @hexKey: 密码使用的 hex 编码；
	     * @return: 返回密文使用 hex 编码；
	     */
	    decrypt: function(eHexContent, hexKey, ivKey) {
	        var _key = CryptoJS.enc.Hex.parse(hexKey);
	        var _iv = CryptoJS.enc.Utf8.parse(ivKey);
	        eHexContent = {
	            ciphertext: CryptoJS.enc.Hex.parse(eHexContent)
	        };
	        eHexContent = CryptoJS.AES.decrypt(eHexContent, _key, {
	            iv: _iv,
	    		mode: CryptoJS.mode.CBC,
	            padding: CryptoJS.pad.Pkcs7
	        });
	        return eHexContent.toString(CryptoJS.enc.Utf8);
	    }
	};

	/*** 将方法对象扩展到 $zk ***/
	// var intermediaryObj = $zk;
	// intermediaryObj.AES = AES;
	_zk.extend({
		AES:AES
	});

})($zk);



