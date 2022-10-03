knkxPrinter = function(){
    var connector = this;
    connector.knkxPrinter = function(urlPdf) {
        console.log("pdf " + urlPdf);
        //printJS(urlPdf)
//        connector.closeCallback(urlPdf)
        printJS({
            printable: urlPdf,
            type: 'pdf',
            onPrintDialogClose: () => connector.closeCallback(urlPdf)
        });
    }
}