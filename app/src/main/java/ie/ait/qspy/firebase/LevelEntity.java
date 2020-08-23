package ie.ait.qspy.firebase;

public class LevelEntity {

    private String description;
    private Long rangeStart;
    private Long rangeEnd;
    private String avatarImg;

    public LevelEntity(String description, Long rangeStart, Long rangeEnd, String avatarImg) {
        this.description = description;
        this.rangeStart = rangeStart;
        this.rangeEnd = rangeEnd;
        this.avatarImg = avatarImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(Long rangeStart) {
        this.rangeStart = rangeStart;
    }

    public long getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(Long rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    public String getAvatarImg() {
        return avatarImg;
    }

    public void setAvatarImg(String avatarImg) {
        this.avatarImg = avatarImg;
    }
}
