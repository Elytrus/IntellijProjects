package me.tlwv2.kitsp.folders;

public class PageData {
    private String currentPage;
    private int pageNum;
    private PageType type;

    public PageData(int pageNum) {
        this("", pageNum, PageType.EDIT_KIT_SELECTOR);
    }

    public PageData(String currentPage, boolean edit){
        this(currentPage, -1, edit ? PageType.FOLDER_EDIT : PageType.FOLDER_SELECT);
    }

    public PageData(String currentPage){
        this(currentPage, false);
    }

    public PageData(){
        this("", -1, PageType.ROOT);
    }

    public PageData(String currentPage, int pageNum, PageType type){
        this.currentPage = currentPage;
        this.pageNum = pageNum;
        this.type = type;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public PageType getType() {
        return type;
    }

    public void setType(PageType type) {
        this.type = type;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public enum PageType{
        EDIT_KIT_SELECTOR,
        FOLDER_EDIT,
        FOLDER_SELECT,
        ROOT;
    }
}
