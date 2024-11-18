import type { EmoticonPackInList } from './emoticonPackInList';

type Sort = {
  sorted: boolean;
  empty: boolean;
  unsorted: boolean;
}

type Pageable = {
  paged: boolean;
  pageNumber: number;
  pageSize: number;
  offset: number;
  sort: Sort;
  unpaged: boolean;
}

type EmoticonPackSearchList = {
  totalElements: number;
  totalPages: number;
  pageable: Pageable;
  size: number;
  content: EmoticonPackInList[];
  number: number;
  sort: Sort;
  numberOfElements: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

export type {
  EmoticonPackSearchList,
  Pageable,
  Sort
};
