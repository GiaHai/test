package com.company.truonghoc.utils.aspose;

import com.aspose.words.*;

import java.util.ArrayList;
import java.util.Collections;

public class SectionSplitter extends DocumentVisitor {

    private PageNumberFinder pageNumberFinder;

    public SectionSplitter(PageNumberFinder pageNumberFinder) {
        this.pageNumberFinder = pageNumberFinder;
    }

    public int visitParagraphStart(Paragraph paragraph) throws Exception {
        return this.continueIfCompositeAcrossPageElseSkip(paragraph);
    }

    public int visitTableStart(Table table) throws Exception {
        return this.continueIfCompositeAcrossPageElseSkip(table);
    }

    public int visitRowStart(Row row) throws Exception {
        return this.continueIfCompositeAcrossPageElseSkip(row);
    }

    public int visitCellStart(Cell cell) throws Exception {
        return this.continueIfCompositeAcrossPageElseSkip(cell);
    }

    public int visitStructuredDocumentTagStart(StructuredDocumentTag sdt) throws Exception {
        return this.continueIfCompositeAcrossPageElseSkip(sdt);
    }

    public int visitSmartTagStart(SmartTag smartTag) throws Exception {
        return this.continueIfCompositeAcrossPageElseSkip(smartTag);
    }

    public int visitSectionStart(Section section) throws Exception {
        Section previousSection = (Section) section.getPreviousSibling();

        // If there is a previous section attempt to copy any linked header footers otherwise they will not appear in an
        // extracted document if the previous section is missing.
        if (previousSection != null) {
            HeaderFooterCollection previousHeaderFooters = previousSection.getHeadersFooters();
            if (!section.getPageSetup().getRestartPageNumbering()) {
                section.getPageSetup().setRestartPageNumbering(true);
                section.getPageSetup().setPageStartingNumber(previousSection.getPageSetup().getPageStartingNumber() + this.pageNumberFinder.pageSpan(previousSection));
            }

            for (HeaderFooter previousHeaderFooter : (Iterable<HeaderFooter>) previousHeaderFooters) {
                if (section.getHeadersFooters().getByHeaderFooterType(previousHeaderFooter.getHeaderFooterType()) == null) {
                    HeaderFooter newHeaderFooter = (HeaderFooter) previousHeaderFooters.getByHeaderFooterType(previousHeaderFooter.getHeaderFooterType()).deepClone(true);
                    section.getHeadersFooters().add(newHeaderFooter);
                }
            }
        }

        return this.continueIfCompositeAcrossPageElseSkip(section);
    }

    public int visitSmartTagEnd(SmartTag smartTag) throws Exception {
        this.splitComposite(smartTag);
        return VisitorAction.CONTINUE;
    }

    public int visitStructuredDocumentTagEnd(StructuredDocumentTag sdt) throws Exception {
        this.splitComposite(sdt);
        return VisitorAction.CONTINUE;
    }

    public int visitCellEnd(Cell cell) throws Exception {
        this.splitComposite(cell);
        return VisitorAction.CONTINUE;
    }

    public int visitRowEnd(Row row) throws Exception {
        this.splitComposite(row);
        return VisitorAction.CONTINUE;
    }

    public int visitTableEnd(Table table) throws Exception {
        this.splitComposite(table);
        return VisitorAction.CONTINUE;
    }

    public int visitParagraphEnd(Paragraph paragraph) throws Exception {
        // If paragraph contains only section break, add fake run into
        if (paragraph.isEndOfSection() && paragraph.getChildNodes().getCount() == 1 && "\f".equals(paragraph.getChildNodes().get(0).getText())) {
            Run run = new Run(paragraph.getDocument());
            paragraph.appendChild(run);
            int currentEndPageNum = this.pageNumberFinder.getPageEnd(paragraph);
            this.pageNumberFinder.addPageNumbersForNode(run, currentEndPageNum, currentEndPageNum);
        }

        for (Paragraph clonePara : (Iterable<Paragraph>) splitComposite(paragraph)) {
            // Remove list numbering from the cloned paragraph but leave the indent the same
            // as the paragraph is supposed to be part of the item before.
            if (paragraph.isListItem()) {
                double textPosition = clonePara.getListFormat().getListLevel().getTextPosition();
                clonePara.getListFormat().removeNumbers();
                clonePara.getParagraphFormat().setLeftIndent(textPosition);
            }
            // Reset spacing of split paragraphs in tables as additional spacing may cause them to look different.
            if (paragraph.isInCell()) {
                clonePara.getParagraphFormat().setSpaceBefore(0);
                paragraph.getParagraphFormat().setSpaceAfter(0);
            }
        }

        return VisitorAction.CONTINUE;
    }

    public int visitSectionEnd(Section section) throws Exception {
        for (Section cloneSection : (Iterable<Section>) this.splitComposite(section)) {
            cloneSection.getPageSetup().setSectionStart(SectionStart.NEW_PAGE);
            cloneSection.getPageSetup().setRestartPageNumbering(true);
            cloneSection.getPageSetup().setPageStartingNumber(section.getPageSetup().getPageStartingNumber() +
                    (section.getDocument().indexOf(cloneSection) - section.getDocument().indexOf(section)));
            cloneSection.getPageSetup().setDifferentFirstPageHeaderFooter(false);

            // corrects page break on end of the section
            SplitPageBreakCorrector.processSection(cloneSection);
        }

        // corrects page break on end of the section
        SplitPageBreakCorrector.processSection(section);

        // Add new page numbering for the body of the section as well.
        this.pageNumberFinder.addPageNumbersForNode(section.getBody(), this.pageNumberFinder.getPage(section), this.pageNumberFinder.getPageEnd(section));
        return VisitorAction.CONTINUE;
    }

    private int continueIfCompositeAcrossPageElseSkip(CompositeNode composite) throws Exception {
        return (this.pageNumberFinder.pageSpan(composite) > 1) ? VisitorAction.CONTINUE : VisitorAction.SKIP_THIS_NODE;
    }

    private ArrayList splitComposite(CompositeNode composite) throws Exception {
        ArrayList splitNodes = new ArrayList</* unknown Type use JavaGenericArguments */>();
        for (Node splitNode : (Iterable<Node>) this.findChildSplitPositions(composite)) {
            splitNodes.add(this.splitCompositeAtNode(composite, splitNode));
        }

        return splitNodes;
    }

    private ArrayList findChildSplitPositions(CompositeNode node) throws Exception {
        // A node may span across multiple pages so a list of split positions is returned.
        // The split node is the first node on the next page.
        ArrayList splitList = new ArrayList();
        int startingPage = this.pageNumberFinder.getPage(node);
        Node[] childNodes = node.getNodeType() == NodeType.SECTION
                ? ((Section) node).getBody().getChildNodes().toArray()
                : node.getChildNodes().toArray();
        for (Node childNode : childNodes) {
            int pageNum = this.pageNumberFinder.getPage(childNode);

            if (childNode instanceof Run) {
                pageNum = this.pageNumberFinder.getPageEnd(childNode);
            }

            // If the page of the child node has changed then this is the split position. Add
            // this to the list.
            if (pageNum > startingPage) {
                splitList.add(childNode);
                startingPage = pageNum;
            }

            if (this.pageNumberFinder.pageSpan(childNode) > 1) {
                this.pageNumberFinder.addPageNumbersForNode(childNode, pageNum, pageNum);
            }
        }

        // Split composites backward so the cloned nodes are inserted in the right order.
        Collections.reverse(splitList);
        return splitList;
    }

    private CompositeNode splitCompositeAtNode(CompositeNode baseNode, Node targetNode) throws Exception {
        CompositeNode cloneNode = (CompositeNode) baseNode.deepClone(false);
        Node node = targetNode;
        int currentPageNum = this.pageNumberFinder.getPage(baseNode);

        // Move all nodes found on the next page into the copied node. Handle row nodes separately.
        if (baseNode.getNodeType() != NodeType.ROW) {
            CompositeNode composite = cloneNode;
            if (baseNode.getNodeType() == NodeType.SECTION) {
                cloneNode = (CompositeNode) baseNode.deepClone(true);
                Section section = (Section) cloneNode;
                section.getBody().removeAllChildren();
                composite = section.getBody();
            }

            while (node != null) {
                Node nextNode = node.getNextSibling();
                composite.appendChild(node);
                node = nextNode;
            }
        } else {
            // If we are dealing with a row then we need to add in dummy cells for the cloned row.
            int targetPageNum = this.pageNumberFinder.getPage(targetNode);
            Node[] childNodes = baseNode.getChildNodes().toArray();
            for (Node childNode : childNodes) {
                int pageNum = this.pageNumberFinder.getPage(childNode);
                if (pageNum == targetPageNum) {
                    cloneNode.getLastChild().remove();
                    cloneNode.appendChild(childNode);
                } else if (pageNum == currentPageNum) {
                    cloneNode.appendChild(childNode.deepClone(false));
                    if (cloneNode.getLastChild().getNodeType() != NodeType.CELL) {
                        ((CompositeNode) cloneNode.getLastChild()).appendChild(((CompositeNode) childNode).getFirstChild().deepClone(false));
                    }
                }
            }
        }

        // Insert the split node after the original.
        baseNode.getParentNode().insertAfter(cloneNode, baseNode);

        // Update the new page numbers of the base node and the clone node including its descendents.
        // This will only be a single page as the cloned composite is split to be on one page.
        int currentEndPageNum = this.pageNumberFinder.getPageEnd(baseNode);
        this.pageNumberFinder.addPageNumbersForNode(baseNode, currentPageNum, currentEndPageNum - 1);
        this.pageNumberFinder.addPageNumbersForNode(cloneNode, currentEndPageNum, currentEndPageNum);

        for (Node childNode : (Iterable<Node>) cloneNode.getChildNodes(NodeType.ANY, true)) {
            this.pageNumberFinder.addPageNumbersForNode(childNode, currentEndPageNum, currentEndPageNum);
        }

        return cloneNode;
    }
}
