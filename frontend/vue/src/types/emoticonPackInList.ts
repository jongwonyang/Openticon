export type EmoticonPackInList = {
  id: number;
  title: string;
  member: {
    id: number;
    email: string;
    nickname: string;
  };
  price: number;
  view: number;
  category: "REAL" | "CHARACTER" | "ENTERTAINMENT" | "LETTER";
  thumbnailImg: string;
  listImg: string;
  description: string;
  shareLink: string;
  createdAt: string;
  updatedAt: string;
  tags: string[];
  public: boolean;
  blacklist: boolean;
  aiGenerated: boolean;
};
