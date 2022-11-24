function loadCompleteFile()
{
    var inputHtmlId = getParamFromUrl('inputid');
    setTimeout(function(){
      var base64Content = parent.document.getElementById(inputHtmlId).value;
      if(base64Content != null && base64Content != "")
      {
        localStorage.setItem(inputHtmlId, base64Content);
      }
      else
      {
        base64Content = localStorage.getItem(inputHtmlId);
      }
      var fileBuffers = _base64ToUint8Array(base64Content);
      PDFViewerApplication.open(fileBuffers);
    }, 100);
}

function loadCompleteFileRemote()
{
    var queryString = location.search.split('?')[1];
    //var inputHtmlId = getParamFromUrl('inputid');
    //var cachedkey = getParamFromUrl('ckey');
    var jumpEncode = getParamFromUrl('jump');
    setTimeout(function(){
      //var fileRemoteContent = parent.document.getElementById(inputHtmlId).value;
      //var fileRemoteId = parent.document.getElementById(inputHtmlId).alt;
      //if(fileRemoteContent != null && fileRemoteContent != "") {
      //  localStorage.setItem(cachedkey, fileRemoteContent);
      //} else {
      //  fileRemoteContent = localStorage.getItem(cachedkey);
      //}
      //PDFViewerApplication.open(fileRemoteContent);

      //++Jump tới vị trí chỉ định
      setTimeout(function(){
          if(jumpEncode != null) {
              jumpEncode = jumpEncode.replace(" ","+");
              var decodeJump = JSON.parse(Base64.decode(jumpEncode));
              var fOption = {
                  begin: decodeJump.s,
                  end: decodeJump.e,
                  type: '',
                  typeLocation: true,
                  query: decodeJump.c,
                  phraseSearch: true,
                  caseSensitive: true,
                  entireWord: true,
                  highlightAll: false,
                  findPrevious: false,
                  showToolBar: false
              };
              PDFViewerApplication.customWebViewerSearchPDFPosition(fOption);
          }
      }, 5000);
    }, 100);
}

function _base64ToUint8Array(base64) {
    var binary_string =  window.atob(base64);
    var len = binary_string.length;
    var bytes = new Uint8Array( len );
    for (var i = 0; i < len; i++)        {
        bytes[i] = binary_string.charCodeAt(i);
    }
    return bytes;
}

function getParamFromUrl(paraName)
{
    try {
        var url_string = window.location.href;
        var url = new URL(url_string);
        var paraValue = url.searchParams.get(paraName);
        return paraValue;
    } catch(ex) {
        return null;
    }
}