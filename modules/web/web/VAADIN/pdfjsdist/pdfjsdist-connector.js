com_company_knkx_web_tailieu_BanAn_InputEdit  = function () {
      var connector = this;
        var element = connector.getElement();
        element.innerHTML = '<iframe allowfullscreen webkitallowfullscreen height="100%" width="100%" id="resultFrame" src="VAADIN/pdfjsdist/web/viewer.html?inputid=hdfIframeFileContent" style="width: 100%;height:100%;"></iframe><input type="hidden" id="hdfIframeFileContent" value="" />';


        connector.onStateChange = function () {
            var state = connector.getState();

            var iframeWindow = document.getElementById("resultFrame").contentWindow;
            if(typeof(state.data.options) != "undefined") {
                var src = document.getElementById("resultFrame").src;
                src += "&file=" + state.data.options.fileUrl;
                src += "&ckey=" + state.data.options.cachedkey;
                if(typeof(state.data.options.jump) != "undefined") {
                    src += "&jump=" + state.data.options.jump;
                }
                if(typeof(state.data.options.page) != "undefined") {
                    src += "#page=" + state.data.options.page;
                }
                document.getElementById("resultFrame").src = src;
            }

            this.webViewerSearchPDFPosition = function (searchString, begin, end) {
                var fOption = {
                    begin: begin,
                    end: end,
                    type: '',
                    typeLocation: true,
                    query: searchString,
                    phraseSearch: true,
                    caseSensitive: true,
                    entireWord: false,
                    highlightAll: true,
                    findPrevious: false,
                    showToolBar: true
                };
                iframeWindow.PDFViewerApplication.customWebViewerSearchPDFPosition(fOption);
            };

            this.findAndHighlightOffsets = function(params) {
                 var listOffsets = [];
                 var objParams = JSON.parse(params);
                 if(Array.isArray(objParams)) {
                    for(var index = 0; index < objParams.length; index++) {
                        listOffsets.push(objParams[index]);
                    }
                 } else {
                    listOffsets.push(objParams);
                 }
                 iframeWindow.PDFViewerApplication.findController.executeCommandHightLight('hightLight', {
                    listOffs: listOffsets
                 });
            };

            this.loadResourceFileViaURL = function(fileUrl) {
                iframeWindow.PDFViewerApplication.customLoadResourceFile(fileUrl, false);
            };

            this.loadResourceFileViaBase64 = function(base64Encode) {
                var hiddenContent = document.getElementById("hdfIframeFileContent");
                hiddenContent.value = base64Encode;
            };

            this.loadResourceFileViaXHRBlob = function(file) {
                iframeWindow.PDFViewerApplication.setTitleUsingUrl(file);
                var xhr = new XMLHttpRequest();
                xhr.onload = function () {
                    iframeWindow.PDFViewerApplication.open(new Uint8Array(xhr.response));
                };
                try {
                    xhr.open('GET', file);
                    xhr.responseType = 'arraybuffer';
                    xhr.send();
                } catch (ex) {
                    throw ex;
                }
            }

            function _base64ToArrayBuffer(base64) {
                var binary_string =  window.atob(base64);
                var len = binary_string.length;
                var bytes = new Uint8Array( len );
                for (var i = 0; i < len; i++)        {
                    bytes[i] = binary_string.charCodeAt(i);
                }
                return bytes.buffer;
            }
        }
};