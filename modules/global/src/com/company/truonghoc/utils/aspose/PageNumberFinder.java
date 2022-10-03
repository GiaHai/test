package com.company.truonghoc.utils.aspose;

import com.aspose.words.*;

import java.util.*;
import java.util.List;

public class PageNumberFinder {
    // Maps node to a start/end page numbers. This is used to override baseline page numbers provided by collector when document is split.
    private Hashtable nodeStartPageLookup = new Hashtable();
    private Hashtable nodeEndPageLookup = new Hashtable();
    private LayoutCollector collector;

    // Maps page number to a list of nodes found on that page.
    private Hashtable reversePageLookup;

    /// <summary>
    /// Initializes a new instance of the <see cref="PageNumberFinder"/> class.
    /// </summary>
    /// <param name="collector">A collector instance which has layout model records for the document.</param>
    public PageNumberFinder(LayoutCollector collector) {
        this.collector = collector;
    }

    /// <summary>
    /// Gets the document this instance works with.
    /// </summary>
    public Document getDocument() {
        return this.collector.getDocument();
    }

    /// <summary>
    /// Retrieves 1-based index of a page that the node begins on.
    /// </summary>
    /// <param name="node">
    /// The node.
    /// </param>
    /// <returns>
    /// Page index.
    /// </returns>
    public int getPage(Node node) throws Exception {
        return this.nodeStartPageLookup.containsKey(node) ?
                (Integer) this.nodeStartPageLookup.get(node) : this.collector.getStartPageIndex(node);
    }

    /// <summary>
    /// Retrieves 1-based index of a page that the node ends on.
    /// </summary>
    /// <param name="node">
    /// The node.
    /// </param>
    /// <returns>
    /// Page index.
    /// </returns>
    public int getPageEnd(Node node) throws Exception {
        return this.nodeEndPageLookup.containsKey(node) ?
                (Integer) this.nodeEndPageLookup.get(node) :
                this.collector.getEndPageIndex(node);
    }

    /// <summary>
    /// Returns how many pages the specified node spans over. Returns 1 if the node is contained within one page.
    /// </summary>
    /// <param name="node">
    /// The node.
    /// </param>
    /// <returns>
    /// Page index.
    /// </returns>
    public int pageSpan(Node node) throws Exception {
        return this.getPageEnd(node) - this.getPage(node) + 1;
    }

    /// <summary>
    /// Returns a list of nodes that are contained anywhere on the specified page or pages which match the specified node type.
    /// </summary>
    /// <param name="startPage">
    /// The start Page.
    /// </param>
    /// <param name="endPage">
    /// The end Page.
    /// </param>
    /// <param name="nodeType">
    /// The node Type.
    /// </param>
    /// <returns>
    /// The <see cref="IList"/>.
    /// </returns>
    public ArrayList retrieveAllNodesOnPages(int startPage, int endPage, int nodeType) throws Exception {
        if (startPage < 1 || startPage > this.getDocument().getPageCount()) {
            throw new IllegalStateException("'startPage' is out of range");
        }

        if (endPage < 1 || endPage > this.getDocument().getPageCount() || endPage < startPage) {
            throw new IllegalStateException("'endPage' is out of range");
        }

        this.checkPageListsPopulated();
        ArrayList pageNodes = new ArrayList();
        for (int page = startPage; page <= endPage; page++) {
            // Some pages can be empty.
            if (!this.reversePageLookup.containsKey(page)) {
                continue;
            }

            for (Node node : (Iterable<Node>) this.reversePageLookup.get(page)) {
                if (node.getParentNode() != null
                        && (nodeType == NodeType.ANY || node.getNodeType() == nodeType)
                        && !pageNodes.contains(node)) {
                    pageNodes.add(node);
                }
            }
        }

        return pageNodes;
    }

    /// <summary>
    /// Splits nodes which appear over two or more pages into separate nodes so that they still appear in the same way
    /// but no longer appear across a page.
    /// </summary>
    public void splitNodesAcrossPages() throws Exception {
        for (Paragraph paragraph : (Iterable<Paragraph>) this.getDocument().getChildNodes(NodeType.PARAGRAPH, true)) {
            if (this.getPage(paragraph) != this.getPageEnd(paragraph)) {
                this.splitRunsByWords(paragraph);
            }
        }

        this.clearCollector();

        // Visit any composites which are possibly split across pages and split them into separate nodes.
        this.getDocument().accept(new SectionSplitter(this));
    }

    /// <summary>
    /// This is called by <see cref="SectionSplitter"/> to update page numbers of split nodes.
    /// </summary>
    /// <param name="node">
    /// The node.
    /// </param>
    /// <param name="startPage">
    /// The start Page.
    /// </param>
    /// <param name="endPage">
    /// The end Page.
    /// </param>
    void addPageNumbersForNode(Node node, int startPage, int endPage) {
        if (startPage > 0) {
            this.nodeStartPageLookup.put(node, startPage);
        }

        if (endPage > 0) {
            this.nodeEndPageLookup.put(node, endPage);
        }
    }

    private static boolean isHeaderFooterType(Node node) {
        return node.getNodeType() == NodeType.HEADER_FOOTER || node.getAncestor(NodeType.HEADER_FOOTER) != null;
    }

    private void checkPageListsPopulated() throws Exception {
        if (this.reversePageLookup != null) {
            return;
        }

        this.reversePageLookup = new Hashtable();

        // Add each node to a list which represent the nodes found on each page.
        for (Node node : (Iterable<Node>) this.getDocument().getChildNodes(NodeType.ANY, true)) {
            // Headers/Footers follow sections. They are not split by themselves.
            if (isHeaderFooterType(node)) {
                continue;
            }

            int startPage = this.getPage(node);
            int endPage = this.getPageEnd(node);
            for (int page = startPage; page <= endPage; page++) {
                if (!this.reversePageLookup.containsKey(page)) {
                    this.reversePageLookup.put(page, new ArrayList());
                }

                ((ArrayList) this.reversePageLookup.get(page)).add(node);
            }
        }
    }

    private void splitRunsByWords(Paragraph paragraph) throws Exception {
        for (Run run : paragraph.getRuns()) {
            if (this.getPage(run) == this.getPageEnd(run)) {
                continue;
            }

            this.splitRunByWords(run);
        }
    }

    private void splitRunByWords(Run run) {
        String[] words = run.getText().split(" ");
        List<String> list = Arrays.asList(words);
        Collections.reverse(list);
        String[] reversedWords = (String[]) list.toArray();

        for (String word : reversedWords) {
            int pos = run.getText().length() - word.length() - 1;
            if (pos > 1) {
                splitRun(run, run.getText().length() - word.length() - 1);
            }
        }
    }

    /// <summary>
    /// Splits text of the specified run into two runs.
    /// Inserts the new run just after the specified run.
    /// </summary>
    private static Run splitRun(Run run, int position) {
        Run afterRun = (Run) run.deepClone(true);
        afterRun.setText(run.getText().substring(position));
        run.setText(run.getText().substring(0, position));
        run.getParentNode().insertAfter(afterRun, run);
        return afterRun;
    }

    private void clearCollector() throws Exception {
        this.collector.clear();
        this.getDocument().updatePageLayout();

        this.nodeStartPageLookup.clear();
        this.nodeEndPageLookup.clear();
    }
}
