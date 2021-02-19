(ns metabase.mbql.schema-test
  (:require [clojure.test :refer :all]
            [metabase.mbql.schema :as mbql.s]
            [schema.core :as s]
            [metabase.test :as mt]))

(defn- valid? [clause]
  (not (s/check mbql.s/field clause)))

(deftest field-clause-test
  (mt/are+ [clause expected] (= expected
                                (not (s/check mbql.s/field clause)))
    [:field 1 nil]                                                          true
    [:field 1 {}]                                                           true
    [:field 1 {:x true}]                                                    true
    [:field 1 2]                                                            false
    [:field "wow" nil]                                                      false
    [:field "wow" {}]                                                       false
    [:field "wow" 1]                                                        false
    [:field "wow" {:base-type :type/Integer}]                               true
    [:field "wow" {:base-type 100}]                                         false
    [:field "wow" {:base-type :type/Integer, :temporal-unit :month}]        true
    [:field "wow" {:base-type :type/Date, :temporal-unit :month}]           true
    [:field "wow" {:base-type :type/DateTimeWithTZ, :temporal-unit :month}] true
    [:field "wow" {:base-type :type/Time, :temporal-unit :month}]           true))
