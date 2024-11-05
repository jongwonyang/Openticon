export type EmoticonPackInList = {
  id: number;
  title: string;
  member: {
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
  public: boolean;
  blacklist: boolean;
  aigenerated: boolean;
};