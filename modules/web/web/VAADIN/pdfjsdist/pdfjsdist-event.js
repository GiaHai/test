function onControlKeyUp(event, searchString, begin, end) {
   //Tổ hợp phím ctrl+f3
   if(event.ctrlKey && event.keyCode == 114){
      var iframeWindow = document.getElementById("resultFrame").contentWindow;

      var fOption = {
          begin: begin,
          end: end,
          type: '',
          typeLocation: true,
          query: searchString,
          phraseSearch: true,
          caseSensitive: true,
          entireWord: searchString,
          highlightAll: false,
          findPrevious: false,
          showToolBar: true
      };
      iframeWindow.PDFViewerApplication.customWebViewerSearchPDFPosition(fOption);
   }
}