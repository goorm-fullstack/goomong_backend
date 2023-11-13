package R.VD.goomong.post.model;

// 커뮤니티, 공지사항, 이벤트, QnA
public enum Type {
    COMMUNITY, NOTICE, EVENT, QNA;

    public Type toType(String str) {
        if (str.equals("COMMUNITY")) return Type.COMMUNITY;
        if (str.equals("NOTICE")) return Type.NOTICE;
        if (str.equals("EVENT")) return Type.EVENT;
        return Type.QNA;
    }
}


