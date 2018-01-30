package com.junbaor.test.model;

import java.util.List;

public class DouBanResponsePo {

    private int count;
    private int start;
    private List<ItemsBean> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        private int layout;
        private TargetBean target;
        private String title;
        private String source;
        private ThemeBean theme;
        private String id;

        public int getLayout() {
            return layout;
        }

        public void setLayout(int layout) {
            this.layout = layout;
        }

        public TargetBean getTarget() {
            return target;
        }

        public void setTarget(TargetBean target) {
            this.target = target;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public ThemeBean getTheme() {
            return theme;
        }

        public void setTheme(ThemeBean theme) {
            this.theme = theme;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public static class TargetBean {
            private String cover_watermark_icon;
            private int photos_count;
            private AuthorBean author;
            private String url;
            private String uri;
            private String cover_url;
            private String cover_watermark_text;
            private int kind;
            private int read_count;
            private String id;
            private String desc;
            private List<?> more_pic_urls;

            public String getCover_watermark_icon() {
                return cover_watermark_icon;
            }

            public void setCover_watermark_icon(String cover_watermark_icon) {
                this.cover_watermark_icon = cover_watermark_icon;
            }

            public int getPhotos_count() {
                return photos_count;
            }

            public void setPhotos_count(int photos_count) {
                this.photos_count = photos_count;
            }

            public AuthorBean getAuthor() {
                return author;
            }

            public void setAuthor(AuthorBean author) {
                this.author = author;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public String getCover_url() {
                return cover_url;
            }

            public void setCover_url(String cover_url) {
                this.cover_url = cover_url;
            }

            public String getCover_watermark_text() {
                return cover_watermark_text;
            }

            public void setCover_watermark_text(String cover_watermark_text) {
                this.cover_watermark_text = cover_watermark_text;
            }

            public int getKind() {
                return kind;
            }

            public void setKind(int kind) {
                this.kind = kind;
            }

            public int getRead_count() {
                return read_count;
            }

            public void setRead_count(int read_count) {
                this.read_count = read_count;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public List<?> getMore_pic_urls() {
                return more_pic_urls;
            }

            public void setMore_pic_urls(List<?> more_pic_urls) {
                this.more_pic_urls = more_pic_urls;
            }

            public static class AuthorBean {
                private int verify_type;
                private String avatar;
                private String name;

                public int getVerify_type() {
                    return verify_type;
                }

                public void setVerify_type(int verify_type) {
                    this.verify_type = verify_type;
                }

                public String getAvatar() {
                    return avatar;
                }

                public void setAvatar(String avatar) {
                    this.avatar = avatar;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }
        }

        public static class ThemeBean {
            private String sharing_url;
            private String icon_url;
            private String uri;
            private String desc;
            private String background_color;
            private String id;
            private String name;

            public String getSharing_url() {
                return sharing_url;
            }

            public void setSharing_url(String sharing_url) {
                this.sharing_url = sharing_url;
            }

            public String getIcon_url() {
                return icon_url;
            }

            public void setIcon_url(String icon_url) {
                this.icon_url = icon_url;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getBackground_color() {
                return background_color;
            }

            public void setBackground_color(String background_color) {
                this.background_color = background_color;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
