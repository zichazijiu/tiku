package com.songzi.web.rest.vm;

public class SubjectVM {

    private Long id;

    private String name;

    private String title;

    private String description;

    private String status;

    private String type;

    private Long right;

    private String totalPoint;

    private String options;

    /**
	 * @return the totalPoint
	 */
	public String getTotalPoint() {
		return totalPoint;
	}

	/**
	 * @param totalPoint the totalPoint to set
	 */
	public void setTotalPoint(String totalPoint) {
		this.totalPoint = totalPoint;
    }
    
    public Long getId() {
        return id;
    }

	public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getRight() {
        return right;
    }

    public void setRight(Long right) {
        this.right = right;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
