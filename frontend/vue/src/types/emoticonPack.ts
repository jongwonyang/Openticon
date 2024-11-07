export type EmoticonPack = {
  id: number;
  title: string;
  author: {
    id: number;
    nickname: string;
    profile: string;
  };
  price: number;
  view: number;
  category: "REAL" | "CHARACTER" | "ENTERTAINMENT" | "LETTER";
  thumbnailImg: string;
  listImg: string;
  emoticons: string[];
  description: string;
  createdAt: string; // ISO 8601 형식의 날짜 문자열
  tags: string[];
  sharedLink: string;
  public: boolean;
  blacklist: boolean;
  aigenerated: boolean;
};