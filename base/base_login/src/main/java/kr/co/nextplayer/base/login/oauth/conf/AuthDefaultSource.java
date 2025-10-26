package kr.co.nextplayer.base.login.oauth.conf;

public enum AuthDefaultSource implements AuthSource {
	
    /**
     * NAVER
     */
    NAVER {
    	private String authUrl = "https://nid.naver.com/oauth2.0/authorize";
    	private String accessUrl = "https://nid.naver.com/oauth2.0/token";
        private String profileUrl = "https://openapi.naver.com/v1/nid/me";
    	
        @Override
        public String authorize() {
            return authUrl;
        }

        @Override
        public String accessToken() {
            return accessUrl;
        }

        @Override
        public String profileUrl() {
            return profileUrl;
        }

        @Override
        public String userInfo() {
            return "";
        }
        
        @Override
		public void setAuthorize(String url) {
        	this.authUrl = url;
        }
        
        @Override
        public void setAccessToken(String url) {
        	this.accessUrl = url;
        }
    },

    /**
     * KAKAO
     */
    KAKAO {
        private String authUrl = "https://kauth.kakao.com/oauth/authorize";
        private String accessUrl = "https://kauth.kakao.com/oauth/token";
        private String profileUrl = "https://kapi.kakao.com/v2/user/me";

        @Override
        public String authorize() {
            return authUrl;
        }

        @Override
        public String accessToken() {
            return accessUrl;
        }

        @Override
        public String profileUrl() {
            return profileUrl;
        }

        @Override
        public String userInfo() {
            return "";
        }

        @Override
        public void setAuthorize(String url) {
            this.authUrl = url;
        }

        @Override
        public void setAccessToken(String url) {
            this.accessUrl = url;
        }

    },

    /**
     * APPLE
     */
    APPLE {
        private String authUrl = "https://appleid.apple.com/auth/authorize";
        private String accessUrl = "https://appleid.apple.com/auth/token";

        @Override
        public String authorize() {
            return authUrl;
        }

        @Override
        public String accessToken() {
            return accessUrl;
        }

        @Override
        public String userInfo() {
            return "";
        }

        @Override
        public String profileUrl() {
            return null;
        }

        @Override
        public void setAuthorize(String url) {
            this.authUrl = url;
        }

        @Override
        public void setAccessToken(String url) {
            this.accessUrl = url;
        }

    }
}